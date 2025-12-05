package com.accuresoftech.abc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardCountsResponse {

    private Long totalCustomers;
    private Long totalLeads;
    private Long totalInvoices;
    private Long totalTasks;

}