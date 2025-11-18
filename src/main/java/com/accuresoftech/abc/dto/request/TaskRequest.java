package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;


public class TaskRequest {
    private String title;
    private TaskStatus status;
    private Long assigneeId;
    private LocalDate dueDate;
    private Long departmentId;
    
    
	public TaskRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public TaskRequest(String title, TaskStatus status, Long assigneeId, LocalDate dueDate, Long departmentId) {
		super();
		this.title = title;
		this.status = status;
		this.assigneeId = assigneeId;
		this.dueDate = dueDate;
		this.departmentId = departmentId;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public Long getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
    
    
    
}
