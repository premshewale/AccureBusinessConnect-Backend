package com.accuresoftech.abc.dto.response;

import com.accuresoftech.abc.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;


public class TaskResponse {
    private Long id;
    private String title;
    private TaskStatus status;
    private String assigneeName;
    private Long assigneeId;
    private LocalDate dueDate;
    private String departmentName;
    private Long departmentId;
    
	public TaskResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskResponse(Long id, String title, TaskStatus status, String assigneeName, Long assigneeId,
			LocalDate dueDate, String departmentName, Long departmentId) {
		super();
		this.id = id;
		this.title = title;
		this.status = status;
		this.assigneeName = assigneeName;
		this.assigneeId = assigneeId;
		this.dueDate = dueDate;
		this.departmentName = departmentName;
		this.departmentId = departmentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
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

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
    
    
}

