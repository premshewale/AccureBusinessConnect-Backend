package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;
import com.accuresoftech.abc.dto.response.DepartmentDashboardResponse;

public interface DashboardService {
    DashboardCountsResponse getAggregatedCounts();
    DepartmentDashboardResponse getDepartmentDashboard(Long departmentId);
}
