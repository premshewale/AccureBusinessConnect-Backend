package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByDepartmentId(Long departmentId);
    long countByAssigneeId(Long userId); // TaskRepository
    long countByDepartmentId(Long departmentId);
    long count(); // total 

   
}
