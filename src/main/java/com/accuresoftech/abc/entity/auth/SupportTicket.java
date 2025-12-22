package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;
import com.accuresoftech.abc.entity.auth.*;
import com.accuresoftech.abc.enums.SupportStatus;
import com.accuresoftech.abc.enums.TicketPriority;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "support_tickets")
public class SupportTicket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private SupportStatus status = SupportStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private SupportService service;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToMany
    @JoinTable(
        name = "support_ticket_tags",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<SupportTag> tags;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private LocalDateTime lastReplyAt;

	public SupportTicket() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SupportTicket(Long id, String subject, String description, SupportStatus status, TicketPriority priority,
			Department department, SupportService service, Customer customer, Contact contact, Set<SupportTag> tags,
			User assignedTo, LocalDateTime lastReplyAt) {
		super();
		this.id = id;
		this.subject = subject;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.department = department;
		this.service = service;
		this.customer = customer;
		this.contact = contact;
		this.tags = tags;
		this.assignedTo = assignedTo;
		this.lastReplyAt = lastReplyAt;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public SupportService getService() {
		return service;
	}

	public void setService(SupportService service) {
		this.service = service;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Set<SupportTag> getTags() {
		return tags;
	}

	public void setTags(Set<SupportTag> tags) {
		this.tags = tags;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDateTime getLastReplyAt() {
		return lastReplyAt;
	}

	public void setLastReplyAt(LocalDateTime lastReplyAt) {
		this.lastReplyAt = lastReplyAt;
	}

    
}
