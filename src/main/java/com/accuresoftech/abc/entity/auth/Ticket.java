package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;
import com.accuresoftech.abc.enums.TicketPriority;
import com.accuresoftech.abc.enums.TicketStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // Fields
    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;
    
    
    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;
    
    @Column(nullable = false)
    private boolean deleted = false;


   
	public Ticket() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Ticket(Long id, Customer customer, Contact contact, Department department, String subject,
			String description, TicketPriority priority, TicketStatus status, User assignedTo) {
		super();
		this.id = id;
		this.customer = customer;
		this.contact = contact;
		this.department = department;
		this.subject = subject;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.assignedTo = assignedTo;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
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


	public User getAssignedTo() {
		return assignedTo;
	}


	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	

	

}
