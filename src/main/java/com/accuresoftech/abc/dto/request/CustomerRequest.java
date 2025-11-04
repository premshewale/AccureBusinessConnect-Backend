package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.CustomerStatus;
import com.accuresoftech.abc.enums.CustomerType;
import jakarta.validation.constraints.NotBlank;



public class CustomerRequest {
    @NotBlank
    private String name;
    private String email;
    private String phone;
    private String address;
    private String industry;
    private CustomerType type;
    private CustomerStatus status;
    private String website;
    private String tags;
    private Long assignedUserId;
    private Long departmentId;
    
    
    
    
	public CustomerRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public CustomerRequest(@NotBlank String name, String email, String phone, String address, String industry,
			CustomerType type, CustomerStatus status, String website, String tags, Long assignedUserId,
			Long departmentId) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.industry = industry;
		this.type = type;
		this.status = status;
		this.website = website;
		this.tags = tags;
		this.assignedUserId = assignedUserId;
		this.departmentId = departmentId;
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
	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
	public CustomerStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerStatus status) {
		this.status = status;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
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
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
    
	
    
}
