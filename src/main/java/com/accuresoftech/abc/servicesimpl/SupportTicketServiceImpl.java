package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.*;
import com.accuresoftech.abc.dto.response.*;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.SupportTicketService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepo;
    private final SupportReplyRepository replyRepo;
    private final SupportTagRepository tagRepo;
    private final DepartmentRepository departmentRepo;
    private final CustomerRepository customerRepo;
    private final ContactRepository contactRepo;
    private final AuthUtils authUtils;

    public SupportTicketServiceImpl(
            SupportTicketRepository ticketRepo,
            SupportReplyRepository replyRepo,
            SupportTagRepository tagRepo,
            DepartmentRepository departmentRepo,
            CustomerRepository customerRepo,
            ContactRepository contactRepo,
            AuthUtils authUtils) {
        this.ticketRepo = ticketRepo;
        this.replyRepo = replyRepo;
        this.tagRepo = tagRepo;
        this.departmentRepo = departmentRepo;
        this.customerRepo = customerRepo;
        this.contactRepo = contactRepo;
        this.authUtils = authUtils;
    }

   /* @Override
    public SupportTicketResponse createTicket(SupportTicketRequest request) {
    	if (request.getDepartmentId() == null) {
            throw new RuntimeException("DepartmentId is required");
        }

        if (request.getCustomerId() == null) {
            throw new RuntimeException("CustomerId is required");
        }

        if (request.getContactId() == null) {
            throw new RuntimeException("ContactId is required");
        }
        
        User user = authUtils.getCurrentUser();

        SupportTicket ticket = new SupportTicket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setDepartment(departmentRepo.findById(request.getDepartmentId()).orElse(null));
        ticket.setCustomer(customerRepo.findById(request.getCustomerId()).orElse(null));
        ticket.setContact(contactRepo.findById(request.getContactId()).orElse(null));
        ticket.setAssignedTo(user);

        SupportTicket saved = ticketRepo.save(ticket);
        return toResponse(saved);
    }*/
    
    @Override
    public SupportTicketResponse createTicket(SupportTicketRequest request) {

        if (request.getDepartmentId() == null)
            throw new RuntimeException("DepartmentId is required");

        if (request.getCustomerId() == null)
            throw new RuntimeException("CustomerId is required");

        if (request.getContactId() == null)
            throw new RuntimeException("ContactId is required");

        User user = authUtils.getCurrentUser();

        Department department = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Contact contact = contactRepo.findById(request.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        SupportTicket ticket = new SupportTicket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setDepartment(department);
        ticket.setCustomer(customer);
        ticket.setContact(contact);
        ticket.setAssignedTo(user);

        SupportTicket saved = ticketRepo.save(ticket);
        return toResponse(saved);
    }


    @Override
    public List<SupportTicketResponse> getAllTickets() {
        User user = authUtils.getCurrentUser();
        RoleKey role = user.getRole().getKey();

        List<SupportTicket> tickets = switch (role) {
            case ADMIN -> ticketRepo.findAll();
            case SUB_ADMIN -> ticketRepo.findByDepartmentId(user.getDepartment().getId());
            case STAFF -> ticketRepo.findByAssignedToId(user.getId());
        };

        return tickets.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SupportTicketResponse getTicketById(Long id) {
        SupportTicket ticket = ticketRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return toResponse(ticket);
    }

    @Override
    public SupportReplyResponse addReply(Long ticketId, SupportReplyRequest request) {
        User user = authUtils.getCurrentUser();
        SupportTicket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        SupportReply reply = new SupportReply();
        reply.setTicket(ticket);
        reply.setRepliedBy(user);
        reply.setMessage(request.getMessage());

        ticket.setLastReplyAt(LocalDateTime.now());
        ticketRepo.save(ticket);

        SupportReply saved = replyRepo.save(reply);

        SupportReplyResponse res = new SupportReplyResponse();
        res.setId(saved.getId());
        res.setMessage(saved.getMessage());
        res.setRepliedBy(user.getName());
        res.setRepliedAt(saved.getCreatedAt());
        return res;
    }

    private SupportTicketResponse toResponse(SupportTicket t) {
        SupportTicketResponse r = new SupportTicketResponse();
        r.setId(t.getId());
        r.setSubject(t.getSubject());
        r.setStatus(t.getStatus());
        r.setPriority(t.getPriority());
        r.setAssignedTo(t.getAssignedTo() != null ? t.getAssignedTo().getName() : null);
        r.setDepartmentName(t.getDepartment() != null ? t.getDepartment().getName() : null);
        r.setCustomerName(t.getCustomer() != null ? t.getCustomer().getName() : null);
        r.setContactName(t.getContact() != null ? t.getContact().getFirstName() : null);
        r.setLastReplyAt(t.getLastReplyAt());
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }
}
