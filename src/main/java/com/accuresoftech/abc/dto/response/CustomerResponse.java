package com.accuresoftech.abc.dto.response;

import java.util.List;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String industry;
    private String type;
    private String status;
    private String website;
    private Integer contactPersonCount;
    private String tags;
    private Long assignedUserId;
    private String assignedUserName;
    private Long departmentId;
    private String departmentName;
    private List<ContactResponse> contacts;
    
    
	public CustomerResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerResponse(Long id, String name, String email, String phone, String address, String industry,
			String type, String status, String website, Integer contactPersonCount, String tags, Long assignedUserId,
			String assignedUserName, Long departmentId, String departmentName, List<ContactResponse> contacts) {
		super();
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
		this.tags = tags;
		this.assignedUserId = assignedUserId;
		this.assignedUserName = assignedUserName;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.contacts = contacts;
	}
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
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Long getAssignedUserId() {
		return assignedUserId;
	}
	public void setAssignedUserId(Long assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public String getAssignedUserName() {
		return assignedUserName;
	}
	public void setAssignedUserName(String assignedUserName) {
		this.assignedUserName = assignedUserName;
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
	public List<ContactResponse> getContacts() {
		return contacts;
	}
	public void setContacts(List<ContactResponse> contacts) {
		this.contacts = contacts;
	}
    
    
}

