package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.TaskRequest;
import com.accuresoftech.abc.dto.response.TaskResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Task;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.TaskStatus;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.TaskRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.TaskService;
import com.accuresoftech.abc.utils.AuthUtils;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthUtils authUtils;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           AuthUtils authUtils) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.authUtils = authUtils;
    }

    // ---------------------------------------------------------------------
    // Create Task
    // ---------------------------------------------------------------------
    @Override
    public TaskResponse createTask(TaskRequest request) {
        User currentUser = authUtils.getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStatus(Optional.ofNullable(request.getStatus()).orElse(TaskStatus.TODO));
        task.setDueDate(request.getDueDate());

        // Assign task owner (assignee)
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(currentUser);
        }

        // Department assignment
        if (currentUser.getRole().getKey() == RoleKey.STAFF) {
            task.setDepartment(currentUser.getDepartment());
        } else if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            task.setDepartment(dept);
        }

        Task saved = taskRepository.save(task);
        return convertToResponse(saved);
    }

    // ---------------------------------------------------------------------
    // Get All Tasks (Role-Based Visibility)
    // ---------------------------------------------------------------------
    @Override
    public List<TaskResponse> getAllTasks() {
        User currentUser = authUtils.getCurrentUser();
        List<Task> tasks;

        RoleKey roleKey = currentUser.getRole().getKey();

        switch (roleKey) {
            case ADMIN -> tasks = taskRepository.findAll();
            case SUB_ADMIN -> {
                if (currentUser.getDepartment() != null) {
                    tasks = taskRepository.findByDepartmentId(currentUser.getDepartment().getId());
                } else tasks = List.of();
            }
            case STAFF -> tasks = taskRepository.findByAssigneeId(currentUser.getId());
            default -> throw new RuntimeException("Unknown role");
        }

        return tasks.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // Get Task By ID
    // ---------------------------------------------------------------------
    @Override
    public TaskResponse getTaskById(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!canAccessTask(currentUser, task)) {
            throw new RuntimeException("Access denied");
        }

        return convertToResponse(task);
    }

    // ---------------------------------------------------------------------
    // Update Task
    // ---------------------------------------------------------------------
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        User currentUser = authUtils.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!canAccessTask(currentUser, task)) {
            throw new RuntimeException("Access denied");
        }

        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        }

        Task updated = taskRepository.save(task);
        return convertToResponse(updated);
    }

    // ---------------------------------------------------------------------
    // Delete Task
    // ---------------------------------------------------------------------
    @Override
    public void deleteTask(Long id) {
        User currentUser = authUtils.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!canAccessTask(currentUser, task)) {
            throw new RuntimeException("Access denied");
        }

        taskRepository.delete(task);
    }

    // ---------------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------------
    private boolean canAccessTask(User user, Task task) {
        RoleKey roleKey = user.getRole().getKey();

        return switch (roleKey) {
            case ADMIN -> true;
            case SUB_ADMIN -> task.getDepartment() != null
                    && user.getDepartment() != null
                    && task.getDepartment().getId().equals(user.getDepartment().getId());
            case STAFF -> task.getAssignee() != null
                    && task.getAssignee().getId().equals(user.getId());
        };
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setDueDate(task.getDueDate());

        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeName(task.getAssignee().getName());
        }

        if (task.getDepartment() != null) {
            response.setDepartmentId(task.getDepartment().getId());
            response.setDepartmentName(task.getDepartment().getName());
        }

        return response;
    }
}
