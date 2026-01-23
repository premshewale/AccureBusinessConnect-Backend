package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.TicketPriority;
import java.util.Set;

public class SupportTicketRequest {

    private String subject;
    private String description;
    private TicketPriority priority;
    private Long departmentId;
    private Long serviceId;
    private Long customerId;
    private Long contactId;
    private Set<Long> tagIds;
	public SupportTicketRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SupportTicketRequest(String subject, String description, TicketPriority priority, Long departmentId,
			Long serviceId, Long customerId, Long contactId, Set<Long> tagIds) {
		super();
		this.subject = subject;
		this.description = description;
		this.priority = priority;
		this.departmentId = departmentId;
		this.serviceId = serviceId;
		this.customerId = customerId;
		this.contactId = contactId;
		this.tagIds = tagIds;
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
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
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
	public Set<Long> getTagIds() {
		return tagIds;
	}
	public void setTagIds(Set<Long> tagIds) {
		this.tagIds = tagIds;
	}

    
}
