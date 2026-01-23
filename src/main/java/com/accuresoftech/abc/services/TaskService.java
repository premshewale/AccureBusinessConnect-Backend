package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.TaskRequest;
import com.accuresoftech.abc.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    TaskResponse updateTask(Long id, TaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    //void deleteTask(Long id);
    
    void deactivateTask(Long id);

    void activateTask(Long id);
}
