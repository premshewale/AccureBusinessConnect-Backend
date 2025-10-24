package com.accuresoftech.abc.dto.request;

import com.accuresoftech.abc.entity.auth.Customer.Status;
import com.accuresoftech.abc.entity.auth.Customer.Type;
//import java.lang.reflect.Type;
import java.util.List;

public class CustomerRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String industry;
    private Type type;
    private Status status;
    private String website;
    private Integer contactPersonCount;
    private List<ContactRequest> contacts;

    // Inner DTO for contact details
    public static class ContactRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String role;
        private boolean isPrimary;

        //Constructor for ContactRequest
        public ContactRequest(String firstName, String lastName, String email, String phone, String role, boolean isPrimary) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.role = role;
            this.isPrimary = isPrimary;
        }
        // Getters and Setters for ContactRequest


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

    //constructor
    public CustomerRequest(String name, String email, String phone, String address, String industry, Type type, Status status, String website, Integer contactPersonCount, List<ContactRequest> contacts) {
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
    }

    // Getters and Setters
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

    public List<ContactRequest> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactRequest> contacts) {
        this.contacts = contacts;
    }
}
