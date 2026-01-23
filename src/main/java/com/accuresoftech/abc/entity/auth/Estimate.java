package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.enums.EstimateStatus;
import com.accuresoftech.abc.enums.EstimateType;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "estimates")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // link to customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // optional contact (contact person)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstimateStatus status = EstimateStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    private EstimateType type = EstimateType.STANDARD;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_total", precision = 12, scale = 2)
    private BigDecimal taxTotal = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 12, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo; // who is working on this estimate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateItem> items;

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateAttachment> attachments;

    public Estimate() {}
    
    
    
    

	public Estimate(Long id, Customer customer, Contact contact, String subject, String description,
			EstimateStatus status, EstimateType type, LocalDate issueDate, LocalDate expiryDate, BigDecimal subtotal,
			BigDecimal taxTotal, BigDecimal grandTotal, Department department, User assignedTo, User createdBy,
			List<EstimateItem> items, List<EstimateAttachment> attachments) {
		super();
		this.id = id;
		this.customer = customer;
		this.contact = contact;
		this.subject = subject;
		this.description = description;
		this.status = status;
		this.type = type;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.subtotal = subtotal;
		this.taxTotal = taxTotal;
		this.grandTotal = grandTotal;
		this.department = department;
		this.assignedTo = assignedTo;
		this.createdBy = createdBy;
		this.items = items;
		this.attachments = attachments;
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

	public EstimateStatus getStatus() {
		return status;
	}

	public void setStatus(EstimateStatus status) {
		this.status = status;
	}

	public EstimateType getType() {
		return type;
	}

	public void setType(EstimateType type) {
		this.type = type;
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

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<EstimateItem> getItems() {
		return items;
	}

	public void setItems(List<EstimateItem> items) {
		this.items = items;
	}

	public List<EstimateAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<EstimateAttachment> attachments) {
		this.attachments = attachments;
	}

   
}
