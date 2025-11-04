package com.accuresoftech.abc.dto.response;

public class ContactResponse {

	private Long id;
    private Long customerId;
    private String customerName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private Boolean isPrimary;
    
    
	public ContactResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ContactResponse(Long id, Long customerId, String customerName, String firstName, String lastName,
			String email, String phone, String role, Boolean isPrimary) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.isPrimary = isPrimary;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
    
    
}
