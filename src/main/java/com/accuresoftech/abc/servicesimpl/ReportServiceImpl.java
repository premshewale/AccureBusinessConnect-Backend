package com.accuresoftech.abc.servicesimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.entity.auth.Lead;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.LeadRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final LeadRepository reportRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<Lead> getLeadConversionReport(
            LocalDateTime from,
            LocalDateTime to) {

        User currentUser = getCurrentUser();
        String role = currentUser.getRole().getKey().name();

        List<Lead> leads =
        		reportRepository.findConvertedLeads(from, to);

        // 🔐 Role-based filtering (same as LeadService)
        if (role.equals("SUB_ADMIN")) {
            leads = leads.stream()
                    .filter(l -> l.getDepartment() != null &&
                            l.getDepartment().getId()
                             .equals(currentUser.getDepartment().getId()))
                    .toList();
        }

        if (role.equals("STAFF")) {
            leads = leads.stream()
                    .filter(l -> l.getOwner() != null &&
                            l.getOwner().getId().equals(currentUser.getId()))
                    .toList();
        }

        return leads;
    }
    
    @Override
    public List<Customer> getCustomerGrowthReport(
            LocalDateTime from,
            LocalDateTime to) {

        User currentUser = getCurrentUser();
        String role = currentUser.getRole().getKey().name();

        List<Customer> customers =
                customerRepository.findCustomersForGrowthReport(from, to);

        // 🔐 Role-based filtering (VERY IMPORTANT)
        if (role.equals("SUB_ADMIN")) {
            customers = customers.stream()
                    .filter(c -> c.getDepartment() != null &&
                            c.getDepartment().getId()
                             .equals(currentUser.getDepartment().getId()))
                    .toList();
        }

        if (role.equals("STAFF")) {
            customers = customers.stream()
                    .filter(c -> c.getAssignedUser() != null &&
                            c.getAssignedUser().getId().equals(currentUser.getId()))
                    .toList();
        }

        return customers;
    }
}

