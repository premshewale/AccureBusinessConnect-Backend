package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.EstimateRequest;
import com.accuresoftech.abc.dto.request.EstimateItemRequest;
import com.accuresoftech.abc.dto.response.EstimateItemResponse;
import com.accuresoftech.abc.dto.response.EstimateResponse;
import com.accuresoftech.abc.entity.auth.Estimate;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.EstimateAttachment;
import com.accuresoftech.abc.entity.auth.EstimateItem;
import com.accuresoftech.abc.enums.EstimateStatus;
import com.accuresoftech.abc.enums.EstimateType;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.EstimateRepository;
import com.accuresoftech.abc.repository.EstimateItemRepository;
import com.accuresoftech.abc.repository.EstimateAttachmentRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.services.EstimateService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class EstimateServiceImpl implements EstimateService {

    private final EstimateRepository estimateRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DepartmentRepository departmentRepository;
    private final EstimateItemRepository itemRepository;
    private final AuthUtils authUtils;

    public EstimateServiceImpl(EstimateRepository estimateRepository,
                               UserRepository userRepository,
                               CustomerRepository customerRepository,
                               DepartmentRepository departmentRepository,
                               EstimateItemRepository itemRepository,
                               AuthUtils authUtils) {
        this.estimateRepository = estimateRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.departmentRepository = departmentRepository;
        this.itemRepository = itemRepository;
        this.authUtils = authUtils;
    }

    @Override
    @Transactional
    public EstimateResponse createEstimate(EstimateRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Estimate estimate = new Estimate();
        estimate.setCustomer(customer);
        estimate.setContact(request.getContactId() == null ? null :
            customer.getContacts().stream()
                .filter(c -> c.getId().equals(request.getContactId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Contact not found for customer")));
        estimate.setSubject(request.getSubject());
        estimate.setDescription(request.getDescription());
        estimate.setType(request.getType());
        estimate.setStatus(Optional.ofNullable(request.getStatus()).orElse(EstimateStatus.DRAFT));
        estimate.setIssueDate(request.getIssueDate());
        estimate.setExpiryDate(request.getExpiryDate());

        // department/assignment logic
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            estimate.setAssignedTo(currentUser);
            estimate.setDepartment(currentUser.getDepartment());
        } else {
            if (request.getAssignedToId() != null) {
                User assignee = userRepository.findById(request.getAssignedToId())
                        .orElseThrow(() -> new RuntimeException("Assigned user not found"));
                estimate.setAssignedTo(assignee);
            }
            if (request.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                estimate.setDepartment(dept);
            } else {
                estimate.setDepartment(currentUser.getDepartment());
            }
        }

        estimate.setCreatedBy(currentUser);

        // items — calculate totals
        List<EstimateItem> items = request.getItems() == null ? List.of() :
                request.getItems().stream().map(ir -> {
                    EstimateItem it = new EstimateItem();
                    it.setEstimate(estimate);
                    it.setDescription(ir.getDescription());
                    it.setQuantity(Optional.ofNullable(ir.getQuantity()).orElse(1));
                    it.setUnitPrice(Optional.ofNullable(ir.getUnitPrice()).orElse(BigDecimal.ZERO));
                    it.setTaxRate(Optional.ofNullable(ir.getTaxRate()).orElse(BigDecimal.ZERO));
                    // compute line total: qty * unitPrice + tax
                    BigDecimal qty = BigDecimal.valueOf(it.getQuantity());
                    BigDecimal lineNet = it.getUnitPrice().multiply(qty);
                    BigDecimal tax = lineNet.multiply(it.getTaxRate().divide(BigDecimal.valueOf(100)));
                    it.setLineTotal(lineNet.add(tax));
                    return it;
                }).collect(Collectors.toList());

        estimate.setItems(items);

        // compute subtotal/tax/grand
        BigDecimal subtotal = items.stream()
                .map(EstimateItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // note: lineTotal already includes tax in this simple model; for separate tax, compute separately as needed
        estimate.setSubtotal(subtotal);
        estimate.setTaxTotal(items.stream()
                .map(i -> {
                    BigDecimal qty = BigDecimal.valueOf(i.getQuantity());
                    BigDecimal lineNet = i.getUnitPrice().multiply(qty);
                    return lineNet.multiply(i.getTaxRate().divide(BigDecimal.valueOf(100)));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        estimate.setGrandTotal(estimate.getSubtotal());

        Estimate saved = estimateRepository.save(estimate);
        // items saved by cascade (ensure CascadeType.ALL on items)
        return convertToResponse(saved);
    }

    @Override
    public List<EstimateResponse> getAllEstimates() {
        User currentUser = authUtils.getCurrentUser();
        RoleKey role = currentUser.getRole().getKey();
        List<Estimate> list;
        switch (role) {
            case ADMIN -> list = estimateRepository.findAll();
            case SUB_ADMIN -> {
                if (currentUser.getDepartment() != null)
                    list = estimateRepository.findByDepartmentId(currentUser.getDepartment().getId());
                else list = List.of();
            }
            case STAFF -> list = estimateRepository.findByAssignedToId(currentUser.getId());
            default -> throw new RuntimeException("Unknown role");
        }
        return list.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public EstimateResponse getEstimateById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Estimate e = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found"));
        if (!canAccess(currentUser, e)) throw new RuntimeException("Access denied");
        return convertToResponse(e);
    }

    @Override
    @Transactional
    public EstimateResponse updateEstimate(Long id, EstimateRequest request) {
        User currentUser = authUtils.getCurrentUser();
        Estimate e = estimateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estimate not found"));
        if (!canAccess(currentUser, e)) throw new RuntimeException("Access denied");

        // restrict staff from changing assignedTo/department except minor fields
        e.setSubject(request.getSubject());
        e.setDescription(request.getDescription());
        e.setType(request.getType());
        e.setIssueDate(request.getIssueDate());
        e.setExpiryDate(request.getExpiryDate());

        if (currentUser.getRole().getKey() != RoleKey.STAFF) {
            if (request.getAssignedToId() != null) {
                User assignee = userRepository.findById(request.getAssignedToId())
                        .orElseThrow(() -> new RuntimeException("Assigned user not found"));
                e.setAssignedTo(assignee);
            }
            if (request.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                e.setDepartment(dept);
            }
        }

        // replace items — remove old & add new
        e.getItems().clear();
        List<EstimateItem> newItems = request.getItems() == null ? List.of() :
                request.getItems().stream().map(ir -> {
                    EstimateItem it = new EstimateItem();
                    it.setEstimate(e);
                    it.setDescription(ir.getDescription());
                    it.setQuantity(Optional.ofNullable(ir.getQuantity()).orElse(1));
                    it.setUnitPrice(Optional.ofNullable(ir.getUnitPrice()).orElse(BigDecimal.ZERO));
                    it.setTaxRate(Optional.ofNullable(ir.getTaxRate()).orElse(BigDecimal.ZERO));
                    BigDecimal qty = BigDecimal.valueOf(it.getQuantity());
                    BigDecimal lineNet = it.getUnitPrice().multiply(qty);
                    BigDecimal tax = lineNet.multiply(it.getTaxRate().divide(BigDecimal.valueOf(100)));
                    it.setLineTotal(lineNet.add(tax));
                    return it;
                }).collect(Collectors.toList());
       
        
     // 1️⃣ Clear existing items (safe)
        e.getItems().clear();

        // 2️⃣ Add new items to SAME collection
        for (EstimateItemRequest ir : request.getItems()) {
            EstimateItem it = new EstimateItem();
            it.setEstimate(e);
            it.setDescription(ir.getDescription());
            it.setQuantity(Optional.ofNullable(ir.getQuantity()).orElse(1));
            it.setUnitPrice(Optional.ofNullable(ir.getUnitPrice()).orElse(BigDecimal.ZERO));
            it.setTaxRate(Optional.ofNullable(ir.getTaxRate()).orElse(BigDecimal.ZERO));

            BigDecimal qty = BigDecimal.valueOf(it.getQuantity());
            BigDecimal lineNet = it.getUnitPrice().multiply(qty);
            BigDecimal tax = lineNet.multiply(it.getTaxRate().divide(BigDecimal.valueOf(100)));
            it.setLineTotal(lineNet.add(tax));

            e.getItems().add(it);
        }

        
        // recompute totals
        BigDecimal subtotal = newItems.stream()
                .map(EstimateItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        e.setSubtotal(subtotal);
        e.setTaxTotal(newItems.stream()
                .map(i -> {
                    BigDecimal qty = BigDecimal.valueOf(i.getQuantity());
                    BigDecimal lineNet = i.getUnitPrice().multiply(qty);
                    return lineNet.multiply(i.getTaxRate().divide(BigDecimal.valueOf(100)));
                }).reduce(BigDecimal.ZERO, BigDecimal::add));
        e.setGrandTotal(e.getSubtotal());

        Estimate updated = estimateRepository.save(e);
        return convertToResponse(updated);
    }

    @Override
    public void deleteEstimate(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Estimate e = estimateRepository.findById(id).orElseThrow(() -> new RuntimeException("Estimate not found"));
        if (!canAccess(currentUser, e)) throw new RuntimeException("Access denied");
        estimateRepository.delete(e);
    }

    @Override
    public EstimateResponse sendEstimate(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Estimate e = estimateRepository.findById(id).orElseThrow(() -> new RuntimeException("Estimate not found"));
        if (!canAccess(currentUser, e)) throw new RuntimeException("Access denied");
        e.setStatus(EstimateStatus.SENT);
        estimateRepository.save(e);
        // TODO: integrate mail/send logic
        return convertToResponse(e);
    }

    @Override
    public EstimateResponse convertToInvoice(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Estimate e = estimateRepository.findById(id).orElseThrow(() -> new RuntimeException("Estimate not found"));
        if (!canAccess(currentUser, e)) throw new RuntimeException("Access denied");
        e.setStatus(EstimateStatus.INVOICED);
        estimateRepository.save(e);
        // TODO: call invoice service to create invoice
        return convertToResponse(e);
    }

    private boolean canAccess(User user, Estimate e) {
        RoleKey role = user.getRole().getKey();
        switch (role) {
            case ADMIN: return true;
            case SUB_ADMIN:
                return e.getDepartment() != null && user.getDepartment() != null &&
                        e.getDepartment().getId().equals(user.getDepartment().getId());
            case STAFF:
                // staff only their own created or assigned estimates
                return (e.getAssignedTo() != null && e.getAssignedTo().getId().equals(user.getId())) ||
                       (e.getCreatedBy() != null && e.getCreatedBy().getId().equals(user.getId()));
            default: return false;
        }
    }

    private EstimateResponse convertToResponse(Estimate e) {
        EstimateResponse r = new EstimateResponse();
        r.setId(e.getId());
        r.setCustomerId(e.getCustomer().getId());
        r.setCustomerName(e.getCustomer().getName());
        if (e.getContact() != null) {
            r.setContactId(e.getContact().getId());
            r.setContactName(e.getContact().getFirstName() + " " + e.getContact().getLastName());
        }
        r.setSubject(e.getSubject());
        r.setDescription(e.getDescription());
        r.setType(e.getType() != null ? e.getType().name() : null);
        r.setStatus(e.getStatus() != null ? e.getStatus().name() : null);
        r.setIssueDate(e.getIssueDate() == null ? null : e.getIssueDate().toString());
        r.setExpiryDate(e.getExpiryDate() == null ? null : e.getExpiryDate().toString());
        r.setSubtotal(e.getSubtotal());
        r.setTaxTotal(e.getTaxTotal());
        r.setGrandTotal(e.getGrandTotal());
        if (e.getDepartment() != null) {
            r.setDepartmentId(e.getDepartment().getId());
            r.setDepartmentName(e.getDepartment().getName());
        }
        if (e.getAssignedTo() != null) {
            r.setAssignedToId(e.getAssignedTo().getId());
            r.setAssignedToName(e.getAssignedTo().getName());
        }
        if (e.getItems() != null) {
            r.setItems(e.getItems().stream().map(it -> {
                EstimateItemResponse ir = new EstimateItemResponse();
                ir.setId(it.getId());
                ir.setDescription(it.getDescription());
                ir.setQuantity(it.getQuantity());
                ir.setUnitPrice(it.getUnitPrice());
                ir.setTaxRate(it.getTaxRate());
                ir.setLineTotal(it.getLineTotal());
                return ir;
            }).collect(Collectors.toList()));
        }
        return r;
    }
}
