package com.accuresoftech.abc.dto.response;

import com.accuresoftech.abc.enums.SupportStatus;
import com.accuresoftech.abc.enums.TicketPriority;

import java.time.LocalDateTime;
import java.util.Set;

public class SupportTicketResponse {

    private Long id;
    private String subject;
    private SupportStatus status;
    private TicketPriority priority;
    private String departmentName;
    private String serviceName;
    private String customerName;
    private String contactName;
    private Set<String> tags;
    private String assignedTo;
    private LocalDateTime lastReplyAt;
    private LocalDateTime createdAt;
	public SupportTicketResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SupportTicketResponse(Long id, String subject, SupportStatus status, TicketPriority priority,
			String departmentName, String serviceName, String customerName, String contactName, Set<String> tags,
			String assignedTo, LocalDateTime lastReplyAt, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.subject = subject;
		this.status = status;
		this.priority = priority;
		this.departmentName = departmentName;
		this.serviceName = serviceName;
		this.customerName = customerName;
		this.contactName = contactName;
		this.tags = tags;
		this.assignedTo = assignedTo;
		this.lastReplyAt = lastReplyAt;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public SupportStatus getStatus() {
		return status;
	}
	public void setStatus(SupportStatus status) {
		this.status = status;
	}
	public TicketPriority getPriority() {
		return priority;
	}
	public void setPriority(TicketPriority priority) {
		this.priority = priority;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public LocalDateTime getLastReplyAt() {
		return lastReplyAt;
	}
	public void setLastReplyAt(LocalDateTime lastReplyAt) {
		this.lastReplyAt = lastReplyAt;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
}

