package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.stream.Collectors;

import com.accuresoftech.abc.dto.request.ProjectRequest;
import com.accuresoftech.abc.dto.response.ProjectResponse;
import com.accuresoftech.abc.entity.auth.Project;
import com.accuresoftech.abc.entity.auth.ProjectTag;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.CustomerRepository;
import com.accuresoftech.abc.repository.ProjectRepository;
import com.accuresoftech.abc.repository.ProjectTagRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.ProjectService;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.accuresoftech.abc.entity.auth.Customer;
import com.accuresoftech.abc.enums.ProjectStatus;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProjectTagRepository projectTagRepository;

    // ===================== COMMON HELPERS =====================

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private boolean canAccessProject(Project project, User user) {
        switch (user.getRole().getKey()) {
            case ADMIN:
                return true;

            case SUB_ADMIN:
                return project.getCustomer().getDepartment().getId()
                        .equals(user.getDepartment().getId());

            case STAFF:
                return project.getMembers().stream()
                        .anyMatch(m -> m.getId().equals(user.getId()));

            default:
                return false;
        }
    }

    // ===================== GET ALL PROJECTS =====================

    @Override
    public List<ProjectResponse> getAllProjects() {

        User currentUser = getCurrentUser();
        List<Project> projects;

        switch (currentUser.getRole().getKey()) {
            case ADMIN:
                projects = projectRepository.findByDeletedFalse();
                break;

            case SUB_ADMIN:
                projects = projectRepository.findByDepartmentId(
                        currentUser.getDepartment().getId());
                break;

            case STAFF:
                projects = projectRepository.findByMemberId(currentUser.getId());
                break;

            default:
                projects = List.of();
        }

        return projects.stream()
                .map(this::toProjectResponse)
                .toList();
    }

    // ===================== GET PROJECT BY ID =====================

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User currentUser = getCurrentUser();
        if (!canAccessProject(project, currentUser)) {
            throw new RuntimeException("Access denied to this project");
        }

        return toProjectResponse(project);
    }

    // ===================== CREATE PROJECT =====================

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {

        User currentUser = getCurrentUser();

        // STAFF cannot create projects
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            throw new RuntimeException("Staff cannot create projects");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Set<User> members = request.getMemberIds() != null
                ? userRepository.findAllById(request.getMemberIds())
                        .stream().collect(Collectors.toSet())
                : Set.of();

        Set<ProjectTag> tags = request.getTagIds() != null
                ? projectTagRepository.findAllById(request.getTagIds())
                        .stream().collect(Collectors.toSet())
                : Set.of();

        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setCustomer(customer);
        project.setBillingType(request.getBillingType());
        project.setRatePerHour(request.getRatePerHour());
        project.setEstimatedHours(request.getEstimatedHours());
        project.setStartDate(request.getStartDate());
        project.setDeadline(request.getDeadline());
        project.setMembers(members);
        project.setTags(tags);
        project.setDescription(request.getDescription());
        project.setStatus(ProjectStatus.NOT_STARTED);

        return toProjectResponse(projectRepository.save(project));
    }

    // ===================== UPDATE PROJECT =====================

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User currentUser = getCurrentUser();

        // STAFF cannot update projects
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            throw new RuntimeException("Staff cannot update projects");
        }

        if (!canAccessProject(project, currentUser)) {
            throw new RuntimeException("Access denied");
        }

        project.setProjectName(request.getProjectName());
        project.setBillingType(request.getBillingType());
        project.setRatePerHour(request.getRatePerHour());
        project.setEstimatedHours(request.getEstimatedHours());
        project.setStartDate(request.getStartDate());
        project.setDeadline(request.getDeadline());
        project.setDescription(request.getDescription());

        if (request.getMemberIds() != null) {
            Set<User> members = userRepository.findAllById(request.getMemberIds())
                    .stream().collect(Collectors.toSet());
            project.setMembers(members);
        }

        if (request.getTagIds() != null) {
            Set<ProjectTag> tags = projectTagRepository.findAllById(request.getTagIds())
                    .stream().collect(Collectors.toSet());
            project.setTags(tags);
        }

        return toProjectResponse(projectRepository.save(project));
    }

    // ===================== UPDATE STATUS =====================

    @Override
    @Transactional
    public ProjectResponse updateProjectStatus(Long id, String status) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User currentUser = getCurrentUser();

        // STAFF cannot update status
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            throw new RuntimeException("Staff cannot update project status");
        }

        if (!canAccessProject(project, currentUser)) {
            throw new RuntimeException("Access denied");
        }

        try {
            ProjectStatus newStatus = ProjectStatus.valueOf(status.toUpperCase());
            project.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid project status: " + status);
        }

        return toProjectResponse(projectRepository.save(project));
    }

    // ===================== DELETE PROJECT (SOFT DELETE) =====================

    @Override
    @Transactional
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User currentUser = getCurrentUser();

        // STAFF cannot delete projects
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            throw new RuntimeException("Staff cannot delete projects");
        }

        if (!canAccessProject(project, currentUser)) {
            throw new RuntimeException("Access denied");
        }

        project.setDeleted(true);
        projectRepository.save(project);
    }

    // ===================== RESPONSE MAPPER =====================

    private ProjectResponse toProjectResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .customerName(project.getCustomer().getName())
                .status(project.getStatus())
                .billingType(project.getBillingType())
                .ratePerHour(project.getRatePerHour())
                .estimatedHours(project.getEstimatedHours())
                .startDate(project.getStartDate())
                .deadline(project.getDeadline())
                .members(project.getMembers()
                        .stream().map(User::getName).collect(Collectors.toSet()))
                .tags(project.getTags()
                        .stream().map(ProjectTag::getName).collect(Collectors.toSet()))
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .build();
    }

	
}