package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.TicketPriority;
import com.accuresoftech.abc.enums.TicketStatus;

public class TicketRequest {
    private Long customerId;
    private Long contactId;
    private String subject;
    private String description;
    private TicketPriority priority;
    private TicketStatus status;
    private Long departmentId;
    
	public TicketRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public TicketRequest(Long customerId, Long contactId, String subject, String description, TicketPriority priority,
			TicketStatus status, Long departmentId) {
		super();
		this.customerId = customerId;
		this.contactId = contactId;
		this.subject = subject;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.departmentId = departmentId;
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
	public TicketPriority getPriority() {
		return priority;
	}
	public void setPriority(TicketPriority priority) {
		this.priority = priority;
	}
	public TicketStatus getStatus() {
		return status;
	}
	public void setStatus(TicketStatus status) {
		this.status = status;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
    
    
}
