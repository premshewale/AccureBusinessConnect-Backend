/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.TicketStatus;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.TicketService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthUtils authUtils;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             CustomerRepository customerRepository,
                             ContactRepository contactRepository,
                             DepartmentRepository departmentRepository,
                             AuthUtils authUtils) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.departmentRepository = departmentRepository; 
        this.authUtils = authUtils;
    }

    // ---------------------------------------------------------------------
    // Create Ticket
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse createTicket(TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Contact contact = contactRepository.findById(request.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setContact(contact);
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(Optional.ofNullable(request.getStatus()).orElse(TicketStatus.OPEN));

        // Department assignment logic
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            ticket.setDepartment(currentUser.getDepartment());
        } else if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            ticket.setDepartment(dept);
        }

        Ticket saved = ticketRepository.save(ticket);
        return convertToResponse(saved);
    }

    // ---------------------------------------------------------------------
    // Get All Tickets (Role-Based Access)
    // ---------------------------------------------------------------------
    @Override
    public List<TicketResponse> getAllTickets() {
        User currentUser = authUtils.getCurrentUser();
        RoleKey roleKey = currentUser.getRole().getKey();

        List<Ticket> tickets;

        switch (roleKey) {
            case ADMIN -> tickets = ticketRepository.findAll();
            case SUB_ADMIN -> {
                if (currentUser.getDepartment() != null) {
                    tickets = ticketRepository.findByDepartmentId(currentUser.getDepartment().getId());
                } else tickets = List.of();
            }
            case STAFF -> tickets = ticketRepository.findByDepartmentId(currentUser.getDepartment().getId());
            default -> throw new RuntimeException("Unknown role");
        }

        return tickets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // Get Ticket By ID (with access control)
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse getTicketById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(ticket);
    }

    // ---------------------------------------------------------------------
    // Update Ticket
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        Ticket updated = ticketRepository.save(ticket);
        return convertToResponse(updated);
    }

    // ---------------------------------------------------------------------
    // Delete Ticket
    // ---------------------------------------------------------------------
    @Override
    public void deleteTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticketRepository.delete(ticket);
    }
    
    // ---------------------------------------------------------------------
    // Helper: Role-based access control
    // ---------------------------------------------------------------------
    private boolean canAccessTicket(User user, Ticket ticket) {
        RoleKey roleKey = user.getRole().getKey();

        return switch (roleKey) {
            case ADMIN -> true;
            case SUB_ADMIN, STAFF -> ticket.getDepartment() != null
                    && user.getDepartment() != null
                    && ticket.getDepartment().getId().equals(user.getDepartment().getId());
        };
    }

    // ---------------------------------------------------------------------
    // Convert Entity to Response DTO
    // ---------------------------------------------------------------------
    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority().name());
        response.setStatus(ticket.getStatus().name());

        if (ticket.getCustomer() != null) {
            response.setCustomerId(ticket.getCustomer().getId());
            response.setCustomerName(ticket.getCustomer().getName());
        }

        if (ticket.getContact() != null) {
            response.setContactId(ticket.getContact().getId());
            response.setContactName(ticket.getContact().getFirstName() + " " + ticket.getContact().getLastName());

        }

        if (ticket.getDepartment() != null) {
            response.setDepartmentId(ticket.getDepartment().getId());
            response.setDepartmentName(ticket.getDepartment().getName());
        }

        return response;
    }
}without escalation logic*/






/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.TicketPriority;
import com.accuresoftech.abc.enums.TicketStatus;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.TicketService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            ContactRepository contactRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            AuthUtils authUtils) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.authUtils = authUtils;
    }

    // ---------------------------------------------------------------------
    // CREATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse createTicket(TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Contact contact = contactRepository.findById(request.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCustomer(customer);
        ticket.setContact(contact);
        ticket.setPriority(Optional.ofNullable(request.getPriority()).orElse(TicketPriority.MEDIUM));
        ticket.setStatus(Optional.ofNullable(request.getStatus()).orElse(TicketStatus.OPEN));

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            ticket.setDepartment(dept);
        } else if (currentUser.getDepartment() != null) {
            ticket.setDepartment(currentUser.getDepartment());
        }

        Ticket saved = ticketRepository.save(ticket);
        return convertToResponse(saved);
    }

    // ---------------------------------------------------------------------
    // GET ALL TICKETS (Role-Based Visibility)
    // ---------------------------------------------------------------------
    @Override
    public List<TicketResponse> getAllTickets() {
        User currentUser = authUtils.getCurrentUser();
        RoleKey roleKey = currentUser.getRole().getKey();
        List<Ticket> tickets;

        switch (roleKey) {
            case ADMIN -> tickets = ticketRepository.findAll();
            case SUB_ADMIN -> {
                if (currentUser.getDepartment() != null) {
                    tickets = ticketRepository.findByDepartmentId(currentUser.getDepartment().getId());
                } else {
                    tickets = List.of();
                }
            }
            case STAFF -> {
                if (currentUser.getDepartment() != null) {
                    tickets = ticketRepository.findByDepartmentId(currentUser.getDepartment().getId());
                } else {
                    tickets = List.of();
                }
            }
            default -> throw new RuntimeException("Invalid role");
        }

        return tickets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // GET TICKET BY ID
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse getTicketById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(ticket);
    }

    // ---------------------------------------------------------------------
    // UPDATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        Ticket updated = ticketRepository.save(ticket);
        return convertToResponse(updated);
    }

    // ---------------------------------------------------------------------
    // DELETE TICKET
    // ---------------------------------------------------------------------
    @Override
    public void deleteTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticketRepository.delete(ticket);
    }

    // ---------------------------------------------------------------------
    // ESCALATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse escalateTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        RoleKey currentRole = currentUser.getRole().getKey();

        // Prevent staff from escalating their own tickets
        if (currentRole == RoleKey.STAFF && 
            (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(currentUser.getId()))) {
            throw new RuntimeException("You are not allowed to escalate this ticket");
        }

        // Prevent escalation of closed/resolved tickets
        if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED) {
            throw new RuntimeException("Cannot escalate a closed or resolved ticket");
        }

        // Determine next role to escalate to
        RoleKey nextRole = switch (currentRole) {
            case STAFF -> RoleKey.SUB_ADMIN;
            case SUB_ADMIN -> RoleKey.ADMIN;
            case ADMIN -> null; // no further escalation
        };

        if (nextRole == null) {
            throw new RuntimeException("Already at the highest escalation level (Admin)");
        }

        // Find a user with next role
        User nextLevelUser = userRepository.findFirstByRole_Key(nextRole)
                .orElseThrow(() -> new RuntimeException("Next-level user not found"));

        // Escalate ticket
        ticket.setAssignedTo(nextLevelUser);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket updated = ticketRepository.save(ticket);

        return convertToResponse(updated);
    }

    

    // ---------------------------------------------------------------------
    // HELPER METHODS
    // ---------------------------------------------------------------------
    private boolean canAccessTicket(User user, Ticket ticket) {
        RoleKey roleKey = user.getRole().getKey();
        return switch (roleKey) {
            case ADMIN -> true;
            case SUB_ADMIN, STAFF -> ticket.getDepartment() != null
                    && user.getDepartment() != null
                    && ticket.getDepartment().getId().equals(user.getDepartment().getId());
        };
    }

    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority().name());
        response.setStatus(ticket.getStatus().name());

        if (ticket.getCustomer() != null) {
            response.setCustomerId(ticket.getCustomer().getId());
            response.setCustomerName(ticket.getCustomer().getName());
        }

        if (ticket.getContact() != null) {
            response.setContactId(ticket.getContact().getId());
            response.setContactName(ticket.getContact().getFirstName() + " " + ticket.getContact().getLastName());
        }

        if (ticket.getDepartment() != null) {
            response.setDepartmentId(ticket.getDepartment().getId());
            response.setDepartmentName(ticket.getDepartment().getName());
        }

        if (ticket.getAssignedTo() != null) {
            response.setAssignedTo(ticket.getAssignedTo().getName());
            response.setEscalatedTo(ticket.getAssignedTo().getRole().getKey().name());
        }


        return response;
    }
}*/


/*package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.TicketPriority;
import com.accuresoftech.abc.enums.TicketStatus;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.TicketService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            ContactRepository contactRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            AuthUtils authUtils) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.authUtils = authUtils;
    }

    // ---------------------------------------------------------------------
    // CREATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse createTicket(TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Contact contact = contactRepository.findById(request.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCustomer(customer);
        ticket.setContact(contact);
        ticket.setPriority(Optional.ofNullable(request.getPriority()).orElse(TicketPriority.MEDIUM));
        ticket.setStatus(Optional.ofNullable(request.getStatus()).orElse(TicketStatus.OPEN));

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            ticket.setDepartment(dept);
        } else if (currentUser.getDepartment() != null) {
            ticket.setDepartment(currentUser.getDepartment());
        }

        Ticket saved = ticketRepository.save(ticket);
        return convertToResponse(saved);
    }

    // ---------------------------------------------------------------------
    // GET ALL TICKETS (Role-Based Visibility)
    // ---------------------------------------------------------------------
    @Override
    public List<TicketResponse> getAllTickets() {
        User currentUser = authUtils.getCurrentUser();
        RoleKey roleKey = currentUser.getRole().getKey();
        List<Ticket> tickets;

        switch (roleKey) {
            case ADMIN -> tickets = ticketRepository.findAll();
            case SUB_ADMIN, STAFF -> {
                if (currentUser.getDepartment() != null) {
                    tickets = ticketRepository.findByDepartmentId(currentUser.getDepartment().getId());
                } else {
                    tickets = List.of();
                }
            }
            default -> throw new RuntimeException("Invalid role");
        }

        return tickets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // GET TICKET BY ID
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse getTicketById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(ticket);
    }

    // ---------------------------------------------------------------------
    // UPDATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        Ticket updated = ticketRepository.save(ticket);
        return convertToResponse(updated);
    }

    // ---------------------------------------------------------------------
    // DELETE TICKET
    // ---------------------------------------------------------------------
    @Override
    public void deleteTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticketRepository.delete(ticket);
    }

    // ---------------------------------------------------------------------
    // ESCALATE TICKET
    // ---------------------------------------------------------------------
    @Override
    public TicketResponse escalateTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        RoleKey role = currentUser.getRole().getKey();

        // ❌ Prevent escalating closed/resolved tickets
        if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED) {
            throw new RuntimeException("Cannot escalate a closed or resolved ticket");
        }

        // ----------------------------
        // ROLE RULE 1: STAFF LOGIC
        // ----------------------------
        if (role == RoleKey.STAFF) {

            // Staff → can escalate ONLY their own tickets
            if (ticket.getAssignedTo() == null ||
                    !ticket.getAssignedTo().getId().equals(currentUser.getId())) {
                throw new RuntimeException("You are not allowed to escalate this ticket");
            }

            // Escalate to SUB_ADMIN
            User subAdmin = userRepository.findFirstByRole_Key(RoleKey.SUB_ADMIN)
                    .orElseThrow(() -> new RuntimeException("No Sub Admin found"));

            ticket.setAssignedTo(subAdmin);
            ticket.setStatus(TicketStatus.IN_PROGRESS);

            Ticket updated = ticketRepository.save(ticket);
            return convertToResponse(updated);
        }

        // ----------------------------
        // ROLE RULE 2: SUB_ADMIN LOGIC
        // ----------------------------
        if (role == RoleKey.SUB_ADMIN) {

            // Sub Admin → escalate to Admin
            User admin = userRepository.findFirstByRole_Key(RoleKey.ADMIN)
                    .orElseThrow(() -> new RuntimeException("No Admin found"));

            ticket.setAssignedTo(admin);
            ticket.setStatus(TicketStatus.IN_PROGRESS);

            Ticket updated = ticketRepository.save(ticket);
            return convertToResponse(updated);
        }

        // ----------------------------
        // ROLE RULE 3: ADMIN LOGIC
        // ----------------------------
        if (role == RoleKey.ADMIN) {
            throw new RuntimeException("Admin cannot escalate further");
        }

        throw new RuntimeException("Invalid role");
    }//modified logic for own staff */

   /* @Override
    public TicketResponse escalateTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        RoleKey currentRole = currentUser.getRole().getKey();

        // Prevent staff from escalating tickets they don't own
        if (currentRole == RoleKey.STAFF &&
            (ticket.getAssignedTo() == null || !ticket.getAssignedTo().getId().equals(currentUser.getId()))) {
            throw new RuntimeException("You are not allowed to escalate this ticket");
        }

        // Prevent escalation of closed/resolved tickets
        if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED) {
            throw new RuntimeException("Cannot escalate a closed or resolved ticket");
        }

        // Determine next role to escalate to
        RoleKey nextRole = switch (currentRole) {
            case STAFF -> RoleKey.SUB_ADMIN;
            case SUB_ADMIN -> RoleKey.ADMIN;
            case ADMIN -> null;
        };

        if (nextRole == null) {
            throw new RuntimeException("Already at the highest escalation level (Admin)");
        }

        // Find next-level user
        User nextLevelUser = userRepository.findFirstByRole_Key(nextRole)
                .orElseThrow(() -> new RuntimeException("Next-level user not found"));

        // Prevent assigning same user again
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId().equals(nextLevelUser.getId())) {
            throw new RuntimeException("Ticket is already assigned to this user");
        }

        // Escalate ticket
        ticket.setAssignedTo(nextLevelUser);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket updated = ticketRepository.save(ticket);

        return convertToResponse(updated);
    }//this is 1st logic*/
    
    

    // ---------------------------------------------------------------------
    // HELPER METHODS
    // ---------------------------------------------------------------------
    /*private boolean canAccessTicket(User user, Ticket ticket) {
        if (user.getRole().getKey() == RoleKey.ADMIN) return true;
        if (ticket.getDepartment() == null || user.getDepartment() == null) return false;
        return ticket.getDepartment().getId().equals(user.getDepartment().getId());
    }

    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority().name());
        response.setStatus(ticket.getStatus().name());

        if (ticket.getCustomer() != null) {
            response.setCustomerId(ticket.getCustomer().getId());
            response.setCustomerName(ticket.getCustomer().getName());
        }

        if (ticket.getContact() != null) {
            response.setContactId(ticket.getContact().getId());
            response.setContactName(ticket.getContact().getFirstName() + " " + ticket.getContact().getLastName());
        }

        if (ticket.getDepartment() != null) {
            response.setDepartmentId(ticket.getDepartment().getId());
            response.setDepartmentName(ticket.getDepartment().getName());
        }

        if (ticket.getAssignedTo() != null) {
            String name = ticket.getAssignedTo().getName() != null ? ticket.getAssignedTo().getName() : ticket.getAssignedTo().getEmail();
            response.setAssignedTo(name);
            response.setEscalatedTo(ticket.getAssignedTo().getRole().getKey().name());
        }

        return response;
    }
}this is direct delete no activation deactivation*/


package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.TicketRequest;
import com.accuresoftech.abc.dto.response.TicketResponse;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.TicketPriority;
import com.accuresoftech.abc.enums.TicketStatus;
import com.accuresoftech.abc.repository.*;
import com.accuresoftech.abc.services.TicketService;
import com.accuresoftech.abc.utils.AuthUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            ContactRepository contactRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            AuthUtils authUtils) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.authUtils = authUtils;
    }

    // --------------------------------------------------
    // CREATE TICKET
    // --------------------------------------------------
    @Override
    public TicketResponse createTicket(TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Contact contact = contactRepository.findById(request.getContactId())
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setCustomer(customer);
        ticket.setContact(contact);
        ticket.setPriority(
                request.getPriority() != null ? request.getPriority() : TicketPriority.MEDIUM
        );
        ticket.setStatus(
                request.getStatus() != null ? request.getStatus() : TicketStatus.OPEN
        );
        ticket.setDeleted(false);

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            ticket.setDepartment(dept);
        } else {
            ticket.setDepartment(currentUser.getDepartment());
        }

        return convertToResponse(ticketRepository.save(ticket));
    }

    // --------------------------------------------------
    // GET ALL TICKETS (ROLE + SOFT DELETE)
    // --------------------------------------------------
    @Override
    public List<TicketResponse> getAllTickets() {
        User currentUser = authUtils.getCurrentUser();
        RoleKey role = currentUser.getRole().getKey();

        List<Ticket> tickets = switch (role) {
            case ADMIN -> ticketRepository.findByDeletedFalse();
            case SUB_ADMIN, STAFF -> {
                if (currentUser.getDepartment() == null) yield List.of();
                yield ticketRepository.findByDepartmentIdAndDeletedFalse(
                        currentUser.getDepartment().getId()
                );
            }
        };

        return tickets.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // --------------------------------------------------
    // GET TICKET BY ID
    // --------------------------------------------------
    @Override
    public TicketResponse getTicketById(Long id) {
        User currentUser = authUtils.getCurrentUser();

        Ticket ticket = ticketRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(ticket);
    }

    // --------------------------------------------------
    // UPDATE TICKET
    // --------------------------------------------------
    @Override
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Ticket ticket = ticketRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!canAccessTicket(currentUser, ticket)) {
            throw new RuntimeException("Access denied");
        }

        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        return convertToResponse(ticketRepository.save(ticket));
    }

    // --------------------------------------------------
    // DEACTIVATE (SOFT DELETE)
    // --------------------------------------------------
    @Override
    public void deactivateTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    // --------------------------------------------------
    // ACTIVATE
    // --------------------------------------------------
    @Override
    public void activateTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setDeleted(false);
        ticketRepository.save(ticket);
    }

    // --------------------------------------------------
    // ESCALATION (UNCHANGED)
    // --------------------------------------------------
    @Override
    public TicketResponse escalateTicket(Long id) {
        User currentUser = authUtils.getCurrentUser();

        Ticket ticket = ticketRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        RoleKey role = currentUser.getRole().getKey();

        if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED) {
            throw new RuntimeException("Cannot escalate closed/resolved ticket");
        }

        if (role == RoleKey.STAFF) {
            if (ticket.getAssignedTo() == null ||
                    !ticket.getAssignedTo().getId().equals(currentUser.getId())) {
                throw new RuntimeException("You are not allowed to escalate this ticket");
            }

            User subAdmin = userRepository.findFirstByRole_Key(RoleKey.SUB_ADMIN)
                    .orElseThrow(() -> new RuntimeException("SubAdmin not found"));

            ticket.setAssignedTo(subAdmin);
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            return convertToResponse(ticketRepository.save(ticket));
        }

        if (role == RoleKey.SUB_ADMIN) {
            User admin = userRepository.findFirstByRole_Key(RoleKey.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            ticket.setAssignedTo(admin);
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            return convertToResponse(ticketRepository.save(ticket));
        }

        throw new RuntimeException("Admin cannot escalate further");
    }

    // --------------------------------------------------
    // ACCESS CHECK
    // --------------------------------------------------
    private boolean canAccessTicket(User user, Ticket ticket) {
        if (user.getRole().getKey() == RoleKey.ADMIN) return true;
        if (ticket.getDepartment() == null || user.getDepartment() == null) return false;
        return ticket.getDepartment().getId().equals(user.getDepartment().getId());
    }

    // --------------------------------------------------
    // RESPONSE MAPPER
    // --------------------------------------------------
    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse r = new TicketResponse();
        r.setId(ticket.getId());
        r.setSubject(ticket.getSubject());
        r.setDescription(ticket.getDescription());
        r.setPriority(ticket.getPriority().name());
        r.setStatus(ticket.getStatus().name());

        if (ticket.getCustomer() != null) {
            r.setCustomerId(ticket.getCustomer().getId());
            r.setCustomerName(ticket.getCustomer().getName());
        }
        if (ticket.getContact() != null) {
            r.setContactId(ticket.getContact().getId());
            r.setContactName(
                    ticket.getContact().getFirstName() + " " + ticket.getContact().getLastName()
            );
        }
        if (ticket.getDepartment() != null) {
            r.setDepartmentId(ticket.getDepartment().getId());
            r.setDepartmentName(ticket.getDepartment().getName());
        }
        if (ticket.getAssignedTo() != null) {
            r.setAssignedTo(ticket.getAssignedTo().getName());
            r.setEscalatedTo(ticket.getAssignedTo().getRole().getKey().name());
        }
        return r;
    }
}








