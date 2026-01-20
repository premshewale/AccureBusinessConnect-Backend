package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.request.ProjectRequest;
import com.accuresoftech.abc.dto.response.ProjectResponse;


public interface ProjectService {

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);

    ProjectResponse updateProjectStatus(Long id, String status);
}