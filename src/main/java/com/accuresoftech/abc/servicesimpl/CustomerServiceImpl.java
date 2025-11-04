/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.CustomerService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               UserRepository userRepository,
                               DepartmentRepository departmentRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setTags(request.getTags());

        // Set assigned user if present
        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            customer.setAssignedUser(assignedUser);
        }

        // Set department if present
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            customer.setDepartment(department);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setIndustry(request.getIndustry());
        existingCustomer.setType(request.getType());
        existingCustomer.setStatus(request.getStatus());
        existingCustomer.setWebsite(request.getWebsite());
        existingCustomer.setTags(request.getTags());

        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            existingCustomer.setAssignedUser(assignedUser);
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            existingCustomer.setDepartment(department);
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    // Convert Customer entity -> CustomerResponse
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType().name());
        response.setStatus(customer.getStatus().name());
        response.setWebsite(customer.getWebsite());
        response.setContactPersonCount(customer.getContactPersonCount());
        response.setTags(customer.getTags());

        if (customer.getAssignedUser() != null) {
            response.setAssignedUserId(customer.getAssignedUser().getId());
            response.setAssignedUserName(customer.getAssignedUser().getName());
                   
        }

        if (customer.getDepartment() != null) {
            response.setDepartmentId(customer.getDepartment().getId());
            response.setDepartmentName(customer.getDepartment().getName());
        }

        if (customer.getContacts() != null) {
            response.setContacts(customer.getContacts()
                    .stream()
                    .map(this::convertContactToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    // Convert Contact entity -> ContactResponse
    private ContactResponse convertContactToResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setCustomerId(contact.getCustomer().getId());
        response.setCustomerName(contact.getCustomer().getName());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setRole(contact.getRole().name());
        response.setIsPrimary(contact.getIsPrimary());
        return response;
    }
}without access  validation*/


/**package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.CustomerService;



import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;




import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               UserRepository userRepository,
                               DepartmentRepository departmentRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }
    
    
    
    
    
    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        }

        throw new RuntimeException("User not authenticated");
    }
    
    
    
    
    
    
    
    
    
    

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        User currentUser = getLoggedInUser();

        // STAFF can only create their own customers
        if (!currentUser.getRole().getName().equals("ADMIN") &&
            !currentUser.getRole().getName().equals("SUB_ADMIN") &&
            request.getAssignedUserId() != null &&
            !request.getAssignedUserId().equals(currentUser.getId())) {
            throw new RuntimeException("Staff can only assign customers to themselves");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setTags(request.getTags());

        // Assigned user
        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            customer.setAssignedUser(assignedUser);
        } else if (currentUser.getRole().getName().equals("STAFF")) {
            // staff must assign to self if not specified
            customer.setAssignedUser(currentUser);
        }

        // Department
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            customer.setDepartment(department);
        } else if (currentUser.getRole().getName().equals("STAFF")) {
            customer.setDepartment(currentUser.getDepartment());
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        User currentUser = getLoggedInUser();
        List<Customer> customers;

        switch (currentUser.getRole().getName()) {
            case "ADMIN" -> customers = customerRepository.findAll();
            case "SUB_ADMIN" -> customers = customerRepository.findByDepartmentId(currentUser.getDepartment().getId());
            default -> customers = customerRepository.findByAssignedUserId(currentUser.getId());
        }

        return customers.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        User currentUser = getLoggedInUser();
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        checkAccess(customer, currentUser);
        return convertToResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        User currentUser = getLoggedInUser();
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        checkAccess(existingCustomer, currentUser);

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());
        existingCustomer.setIndustry(request.getIndustry());
        existingCustomer.setType(request.getType());
        existingCustomer.setStatus(request.getStatus());
        existingCustomer.setWebsite(request.getWebsite());
        existingCustomer.setTags(request.getTags());

        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            existingCustomer.setAssignedUser(assignedUser);
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            existingCustomer.setDepartment(department);
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToResponse(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        User currentUser = getLoggedInUser();
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        checkAccess(customer, currentUser);
        customerRepository.delete(customer);
    }

    // ✅ Access check helper
    private void checkAccess(Customer customer, User user) {
        switch (user.getRole().getName()) {
            case "ADMIN" -> { /* full access */ /*}*/
   /*         case "SUB_ADMIN" -> {
                if (!customer.getDepartment().getId().equals(user.getDepartment().getId())) {
                    throw new RuntimeException("Sub-admin cannot access this customer");
                }
            }
            default -> {
                if (!customer.getAssignedUser().getId().equals(user.getId())) {
                    throw new RuntimeException("Staff cannot access this customer");
                }
            }
        }
    }

    // ✅ Convert Entity -> Response DTO
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType().name());
        response.setStatus(customer.getStatus().name());
        response.setWebsite(customer.getWebsite());
        response.setContactPersonCount(customer.getContactPersonCount());
        response.setTags(customer.getTags());

        if (customer.getAssignedUser() != null) {
            response.setAssignedUserId(customer.getAssignedUser().getId());
            response.setAssignedUserName(customer.getAssignedUser().getName());
        }

        if (customer.getDepartment() != null) {
            response.setDepartmentId(customer.getDepartment().getId());
            response.setDepartmentName(customer.getDepartment().getName());
        }

        if (customer.getContacts() != null) {
            response.setContacts(customer.getContacts()
                    .stream()
                    .map(this::convertContactToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private ContactResponse convertContactToResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setCustomerId(contact.getCustomer().getId());
        response.setCustomerName(contact.getCustomer().getName());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setRole(contact.getRole().name());
        response.setIsPrimary(contact.getIsPrimary());
        return response;
    }

}**/






package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.CustomerRequest;
import com.accuresoftech.abc.dto.response.ContactResponse;
import com.accuresoftech.abc.dto.response.CustomerResponse;
import com.accuresoftech.abc.entity.auth.Contact;
import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               UserRepository userRepository,
                               DepartmentRepository departmentRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    // -----------------------------
    // Get logged-in user
    // -----------------------------
    private User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
        }
        throw new RuntimeException("User not authenticated");
    }

    // -----------------------------
    // Create Customer
    // -----------------------------
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        User currentUser = getLoggedInUser();
        RoleKey roleKey = currentUser.getRole().getKey();

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setTags(request.getTags());

        // STAFF can only assign to themselves
        if (roleKey == RoleKey.STAFF) {
            customer.setAssignedUser(currentUser);
            customer.setDepartment(currentUser.getDepartment());
        } else if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            customer.setAssignedUser(assignedUser);
        }

        if (request.getDepartmentId() != null && roleKey != RoleKey.STAFF) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            customer.setDepartment(department);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }

    // -----------------------------
    // Get All Customers
    // -----------------------------
    @Override
    public List<CustomerResponse> getAllCustomers() {
        User currentUser = getLoggedInUser();
        RoleKey roleKey = currentUser.getRole().getKey();
        List<Customer> customers;

        switch (roleKey) {
            case ADMIN -> customers = customerRepository.findAll();
            case SUB_ADMIN -> customers = customerRepository.findAllByDepartmentId(currentUser.getDepartment().getId());
            case STAFF -> customers = customerRepository.findAllByAssignedUserId(currentUser.getId());
            default -> throw new RuntimeException("Unknown role: " + roleKey);
        }

        return customers.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // -----------------------------
    // Get Customer By ID
    // -----------------------------
    @Override
    public CustomerResponse getCustomerById(Long id) {
        User currentUser = getLoggedInUser();
        RoleKey roleKey = currentUser.getRole().getKey();

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!canAccessCustomer(currentUser, customer)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(customer);
    }

    // -----------------------------
    // Update Customer
    // -----------------------------
    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        User currentUser = getLoggedInUser();
        RoleKey roleKey = currentUser.getRole().getKey();

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!canAccessCustomer(currentUser, customer)) {
            throw new RuntimeException("Access denied");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setIndustry(request.getIndustry());
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());
        customer.setWebsite(request.getWebsite());
        customer.setTags(request.getTags());

        if (request.getAssignedUserId() != null && roleKey != RoleKey.STAFF) {
            User assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            customer.setAssignedUser(assignedUser);
        }

        if (request.getDepartmentId() != null && roleKey != RoleKey.STAFF) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            customer.setDepartment(department);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponse(updatedCustomer);
    }

    // -----------------------------
    // Delete Customer
    // -----------------------------
    @Override
    public void deleteCustomer(Long id) {
        User currentUser = getLoggedInUser();
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!canAccessCustomer(currentUser, customer)) {
            throw new RuntimeException("Access denied");
        }

        customerRepository.delete(customer);
    }

    // -----------------------------
    // Access check helper
    // -----------------------------
    private boolean canAccessCustomer(User user, Customer customer) {
        RoleKey roleKey = user.getRole().getKey();
        return switch (roleKey) {
            case ADMIN -> true;
            case SUB_ADMIN -> customer.getDepartment() != null &&
                              user.getDepartment() != null &&
                              customer.getDepartment().getId().equals(user.getDepartment().getId());
            case STAFF -> customer.getAssignedUser() != null &&
                          customer.getAssignedUser().getId().equals(user.getId());
        };
    }

    // -----------------------------
    // Convert Customer -> CustomerResponse
    // -----------------------------
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setIndustry(customer.getIndustry());
        response.setType(customer.getType().name());
        response.setStatus(customer.getStatus().name());
        response.setWebsite(customer.getWebsite());
        response.setContactPersonCount(customer.getContactPersonCount());
        response.setTags(customer.getTags());

        if (customer.getAssignedUser() != null) {
            response.setAssignedUserId(customer.getAssignedUser().getId());
            response.setAssignedUserName(customer.getAssignedUser().getName());
        }

        if (customer.getDepartment() != null) {
            response.setDepartmentId(customer.getDepartment().getId());
            response.setDepartmentName(customer.getDepartment().getName());
        }

        if (customer.getContacts() != null) {
            response.setContacts(customer.getContacts()
                    .stream()
                    .map(this::convertContactToResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    private ContactResponse convertContactToResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setCustomerId(contact.getCustomer().getId());
        response.setCustomerName(contact.getCustomer().getName());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setRole(contact.getRole().name());
        response.setIsPrimary(contact.getIsPrimary());
        return response;
    }
}
