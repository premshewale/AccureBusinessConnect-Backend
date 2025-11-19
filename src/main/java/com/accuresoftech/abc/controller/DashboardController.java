package com.accuresoftech.abc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;
import com.accuresoftech.abc.services.DashboardService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	// This dashboard module provides aggregated counts for Customers, Leads, and Invoices.
	// Tasks count is pending as it is handled by another developer.
	
    private final DashboardService dashboardService;

    @GetMapping("/counts")
    public ResponseEntity<DashboardCountsResponse> getCounts() {
        return ResponseEntity.ok(dashboardService.getAggregatedCounts());
    }
}