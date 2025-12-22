package com.accuresoftech.abc.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class EstimateResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long contactId;
    private String contactName;
    private String subject;
    private String description;
    private String type;
    private String status;
    private String issueDate;
    private String expiryDate;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal grandTotal;
    private Long departmentId;
    private String departmentName;
    private Long assignedToId;
    private String assignedToName;
    private List<EstimateItemResponse> items;
	public EstimateResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EstimateResponse(Long id, Long customerId, String customerName, Long contactId, String contactName,
			String subject, String description, String type, String status, String issueDate, String expiryDate,
			BigDecimal subtotal, BigDecimal taxTotal, BigDecimal grandTotal, Long departmentId, String departmentName,
			Long assignedToId, String assignedToName, List<EstimateItemResponse> items) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
		this.contactId = contactId;
		this.contactName = contactName;
		this.subject = subject;
		this.description = description;
		this.type = type;
		this.status = status;
		this.issueDate = issueDate;
		this.expiryDate = expiryDate;
		this.subtotal = subtotal;
		this.taxTotal = taxTotal;
		this.grandTotal = grandTotal;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.assignedToId = assignedToId;
		this.assignedToName = assignedToName;
		this.items = items;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
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
	public Long getAssignedToId() {
		return assignedToId;
	}
	public void setAssignedToId(Long assignedToId) {
		this.assignedToId = assignedToId;
	}
	public String getAssignedToName() {
		return assignedToName;
	}
	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}
	public List<EstimateItemResponse> getItems() {
		return items;
	}
	public void setItems(List<EstimateItemResponse> items) {
		this.items = items;
	}
    
    
}
