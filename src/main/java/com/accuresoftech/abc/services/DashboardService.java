package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;

public interface DashboardService {
    DashboardCountsResponse getAggregatedCounts();
}