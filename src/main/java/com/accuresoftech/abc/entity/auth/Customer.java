package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.*;
import java.util.List;

import com.accuresoftech.abc.entity.BaseEntity;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String phone;

	@Column(columnDefinition = "TEXT")
	private String address;

	private String industry;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Type type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	private String website;

	@Column(name = "contact_person_count")
	private Integer contactPersonCount;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts;

	
	
	
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner; // Logged-in user who owns this customer

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department; // Optional if Managers handle departments

	
	
	
	
	public enum Type {
		VIP, REGULAR, ONE_TIME
	}

	public enum Status {
		ACTIVE, INACTIVE, PROSPECT, BLOCKED
	}

	public Customer() {
		// JPA requires a default constructor
	}

	// Constructor

	public Customer(Long id, String name, String email, String phone, String address, String industry, Type type,
			Status status, String website, Integer contactPersonCount, List<Contact> contacts) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.industry = industry;
		this.type = type;
		this.status = status;
		this.website = website;
		this.contactPersonCount = contactPersonCount;
		this.contacts = contacts;
		this.owner = owner;
		this.department = department;
		

	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Integer getContactPersonCount() {
		return contactPersonCount;
	}

	public void setContactPersonCount(Integer contactPersonCount) {
		this.contactPersonCount = contactPersonCount;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	
}
