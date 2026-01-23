package com.accuresoftech.abc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCountsResponse {

    private Long totalCustomers;
    private Long totalLeads;
    private Long totalInvoices;
    private Long totalTasks;
    private Long totalTickets;
    private Long totalExpenses;
}
