
package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.accuresoftech.abc.dto.request.ProjectRequest;
import com.accuresoftech.abc.dto.request.ProjectStatusRequest;
import com.accuresoftech.abc.dto.response.ProjectResponse;
import com.accuresoftech.abc.services.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // ===================== GET ALL PROJECTS =====================

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // ===================== GET PROJECT BY ID =====================

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // ===================== CREATE PROJECT =====================

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request) {

        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===================== UPDATE PROJECT =====================

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {

        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    // ===================== UPDATE PROJECT STATUS =====================

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectResponse> updateProjectStatus(
            @PathVariable Long id,
            @Valid @RequestBody ProjectStatusRequest statusRequest) {

        ProjectResponse response =
                projectService.updateProjectStatus(id, statusRequest.getStatus());

        return ResponseEntity.ok(response);
    }

    // ===================== DELETE PROJECT (SOFT DELETE) =====================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
