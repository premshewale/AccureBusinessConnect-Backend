package com.accuresoftech.abc.dto.request;

public class ContactRequest {

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
