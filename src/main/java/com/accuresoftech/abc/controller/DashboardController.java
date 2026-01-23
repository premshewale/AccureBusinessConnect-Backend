package com.accuresoftech.abc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.response.DashboardCountsResponse;
import com.accuresoftech.abc.dto.response.DepartmentDashboardResponse;
import com.accuresoftech.abc.services.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // 🔹 MAIN DASHBOARD
    @GetMapping("/counts")
    public ResponseEntity<DashboardCountsResponse> getCounts() {
        return ResponseEntity.ok(dashboardService.getAggregatedCounts());
    }

    // 🔹 DEPARTMENT DASHBOARD
    @GetMapping("/department/{id}")
    public ResponseEntity<DepartmentDashboardResponse> getDepartmentDashboard(
            @PathVariable Long id) {

        return ResponseEntity.ok(dashboardService.getDepartmentDashboard(id));
    }
}
