package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.enums.ContactRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


public class ContactRequest {
    @NotNull
    private Long customerId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String email;
    private String phone;

    @NotNull
    private ContactRole role;

    private Boolean isPrimary = false;

	public ContactRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactRequest(@NotNull Long customerId, @NotBlank String firstName, @NotBlank String lastName, String email,
			String phone, @NotNull ContactRole role, Boolean isPrimary) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.isPrimary = isPrimary;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public ContactRole getRole() {
		return role;
	}

	public void setRole(ContactRole role) {
		this.role = role;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
    
    
    
}
