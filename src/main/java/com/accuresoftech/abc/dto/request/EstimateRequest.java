package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.EstimateStatus;
import com.accuresoftech.abc.enums.EstimateType;
import java.time.LocalDate;
import java.util.List;

public class EstimateRequest {
    private Long customerId;
    private Long contactId;
    private String subject;
    private String description;
    private EstimateType type;
    private EstimateStatus status;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Long departmentId;
    private Long assignedToId;
    private List<EstimateItemRequest> items;
	public EstimateRequest(Long customerId, Long contactId, String subject, String description, EstimateType type,
			EstimateStatus status, LocalDate issueDate, LocalDate expiryDate, Long departmentId, Long assignedToId,
			List<EstimateItemRequest> items) {
		super();
		this.customerId = customerId;
		this.contactId = contactId;
		this.subject = subject;
		this.description = description;
		this.type = type;
		this.status = status;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.departmentId = departmentId;
		this.assignedToId = assignedToId;
		this.items = items;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EstimateType getType() {
		return type;
	}
	public void setType(EstimateType type) {
		this.type = type;
	}
	public EstimateStatus getStatus() {
		return status;
	}
	public void setStatus(EstimateStatus status) {
		this.status = status;
	}
	public LocalDate getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getAssignedToId() {
		return assignedToId;
	}
	public void setAssignedToId(Long assignedToId) {
		this.assignedToId = assignedToId;
	}
	public List<EstimateItemRequest> getItems() {
		return items;
	}
	public void setItems(List<EstimateItemRequest> items) {
		this.items = items;
	}
   
    
    
}
