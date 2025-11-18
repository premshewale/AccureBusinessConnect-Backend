package com.accuresoftech.abc.dto.response;

public class TicketResponse {
    private Long id;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private Long customerId;
    private String customerName;
    private Long contactId;
    private String contactName;
    private Long departmentId;
    private String departmentName;
    
    private String assignedTo;
    private String escalatedTo;
    
	public TicketResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TicketResponse(Long id, String subject, String description, String priority, String status, Long customerId,
			String customerName, Long contactId, String contactName, Long departmentId, String departmentName,
			String assignedTo, String escalatedTo) {
		super();
		this.id = id;
		this.subject = subject;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.customerId = customerId;
		this.customerName = customerName;
		this.contactId = contactId;
		this.contactName = contactName;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.assignedTo = assignedTo;
		this.escalatedTo = escalatedTo;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getEscalatedTo() {
		return escalatedTo;
	}

	public void setEscalatedTo(String escalatedTo) {
		this.escalatedTo = escalatedTo;
	}


	
	
	
    
    
}
