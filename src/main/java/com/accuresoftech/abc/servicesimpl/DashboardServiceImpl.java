package com.accuresoftech.abc.servicesimpl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;
import com.accuresoftech.abc.dto.response.DepartmentDashboardResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.ExpenseRepository;
import com.accuresoftech.abc.repository.InvoiceRepository;
import com.accuresoftech.abc.repository.LeadRepository;
import com.accuresoftech.abc.repository.TaskRepository;
import com.accuresoftech.abc.repository.TicketRepository;
import com.accuresoftech.abc.services.DashboardService;
import com.accuresoftech.abc.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;
    private final TaskRepository taskRepository;
    private final InvoiceRepository invoiceRepository;
    private final TicketRepository ticketRepository;
    private final ExpenseRepository expenseRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthUtils authUtils;

    // =========================
    // MAIN DASHBOARD (ROLE BASED)
    // =========================
    @Override
    public DashboardCountsResponse getAggregatedCounts() {

        User currentUser = authUtils.getCurrentUser();
        RoleKey role = currentUser.getRole().getKey();

        // -------------------------
        // ADMIN → FULL SYSTEM
        // -------------------------
        if (role == RoleKey.ADMIN) {
            return DashboardCountsResponse.builder()
                    .totalCustomers(customerRepository.count())
                    .totalLeads(leadRepository.count())
                    .totalTasks(taskRepository.count())
                    .totalInvoices(invoiceRepository.count())
                    .totalTickets(ticketRepository.count())
                    .totalExpenses(expenseRepository.count())
                    .build();
        }

        // -------------------------
        // MANAGER → DEPARTMENT
        // -------------------------
        if (role == RoleKey.SUB_ADMIN) {

            if (currentUser.getDepartment() == null) {
                throw new AccessDeniedException("Manager has no department assigned");
            }

            Long deptId = currentUser.getDepartment().getId();

            return DashboardCountsResponse.builder()
                    .totalCustomers(customerRepository.countByDepartmentId(deptId))
                    .totalLeads(leadRepository.countByDepartmentId(deptId))
                    .totalTasks(taskRepository.countByDepartmentId(deptId))
                    .totalInvoices(invoiceRepository.countByDepartmentId(deptId))
                    .totalTickets(ticketRepository.countByDepartmentId(deptId))
                    .totalExpenses(expenseRepository.countByDepartmentId(deptId))
                    .build();
        }

        // -------------------------
        // STAFF → PERSONAL
        // -------------------------
        if (role == RoleKey.STAFF) {

            Long userId = currentUser.getId();

            return DashboardCountsResponse.builder()
                    .totalCustomers(customerRepository.countByAssignedUser_Id(userId))
                    .totalLeads(leadRepository.countByOwner_Id(userId))
                    .totalTasks(taskRepository.countByAssigneeId(userId))
                    .totalInvoices(invoiceRepository.countByCreatedBy_Id(userId))
                    .totalTickets(0L)   // optional: add countByAssignedTo later
                    .totalExpenses(0L)  // optional: add countByOwner later
                    .build();
        }

        throw new AccessDeniedException("Invalid role");
    }

    // =========================
    // DEPARTMENT DASHBOARD
    // =========================
    @Override
    public DepartmentDashboardResponse getDepartmentDashboard(Long departmentId) {

        User currentUser = authUtils.getCurrentUser();
        RoleKey role = currentUser.getRole().getKey();

        // Only ADMIN or SUB_ADMIN allowed
        if (role == RoleKey.STAFF) {
            throw new AccessDeniedException("Staff cannot access department dashboard");
        }

        // SubAdmin can access ONLY own department
        if (role == RoleKey.SUB_ADMIN &&
                !currentUser.getDepartment().getId().equals(departmentId)) {
            throw new AccessDeniedException("Manager can access only own department");
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return DepartmentDashboardResponse.builder()
                .departmentId(department.getId())
                .departmentName(department.getName())
                .customers(customerRepository.countByDepartmentId(departmentId))
                .leads(leadRepository.countByDepartmentId(departmentId))
                .tasks(taskRepository.countByDepartmentId(departmentId))
                .tickets(ticketRepository.countByDepartmentId(departmentId))
                .invoices(invoiceRepository.countByDepartmentId(departmentId))
                .expenses(expenseRepository.countByDepartmentId(departmentId))
                .build();
    }
}
