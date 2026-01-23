package com.accuresoftech.abc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDashboardResponse {

    private Long departmentId;
    private String departmentName;

    private Long customers;
    private Long leads;
    private Long tasks;
    private Long tickets;
    private Long invoices;
    private Long expenses;
}
