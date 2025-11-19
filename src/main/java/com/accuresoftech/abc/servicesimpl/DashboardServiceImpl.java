package com.accuresoftech.abc.servicesimpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.InvoiceRepository;
import com.accuresoftech.abc.repository.LeadRepository;
import com.accuresoftech.abc.services.DashboardService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {  // <-- implement interface
    private final CustomerRepository customerRepo;
    private final LeadRepository leadRepo;
    private final InvoiceRepository invoiceRepo;
    private final AuthUtils authUtils;

    @Override
    public DashboardCountsResponse getAggregatedCounts() {
        User currentUser = authUtils.getCurrentUser();

        // Customers
        Long totalCustomers = switch (currentUser.getRole().getKey()) {
            case ADMIN -> customerRepo.count();
            case SUB_ADMIN -> customerRepo.countByDepartment_Id(currentUser.getDepartment().getId());
            default -> customerRepo.countByAssignedUser_Id(currentUser.getId());
        };

        // Leads
        Long totalLeads = switch (currentUser.getRole().getKey()) {
            case ADMIN -> leadRepo.count();
            case SUB_ADMIN -> leadRepo.countByDepartment_Id(currentUser.getDepartment().getId());
            default -> leadRepo.countByOwner_Id(currentUser.getId());
        };

        // Invoices
        Long totalInvoices = switch (currentUser.getRole().getKey()) {
            case ADMIN -> invoiceRepo.count();
            case SUB_ADMIN -> invoiceRepo.countByDepartment_Id(currentUser.getDepartment().getId());
            default -> invoiceRepo.countByCreatedBy_Id(currentUser.getId());
        };

        // Tasks not available yet
        Long totalTasks = 0L;

        return new DashboardCountsResponse(totalCustomers, totalLeads, totalInvoices, totalTasks);
    }
}