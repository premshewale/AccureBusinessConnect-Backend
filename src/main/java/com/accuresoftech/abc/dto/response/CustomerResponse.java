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
    private List<ContactResponse> contacts;
    
    private String ownerName;
    private String departmentName;

    // Inner DTO for contact details
   public static class ContactResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String role;
        private boolean isPrimary;

        public ContactResponse() {
            // Needed for Spring and Jackson
        }

        //Constructor for ContactRequest
        public ContactResponse(Long id, String firstName, String lastName, String email, String phone, String role, boolean isPrimary) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.role = role;
            this.isPrimary = isPrimary;
        }

        // Getters and Setters for ContactRequest
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public boolean isPrimary() {
            return isPrimary;
        }

        public void setPrimary(boolean primary) {
            isPrimary = primary;
        }
    }

    public CustomerResponse() {
        // Needed for Spring and Jackson
    }


    // Constructor
    public CustomerResponse(Long id, String name, String email, String phone, String address, String industry, String type, String status, String website, Integer contactPersonCount, List<ContactResponse> contacts) {
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
        this.ownerName = ownerName;
        this.departmentName = departmentName;
        
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

    public List<ContactResponse> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactResponse> contacts) {
        this.contacts = contacts;
    }


	public String getOwnerName() {
		return ownerName;
	}


	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
    
}
