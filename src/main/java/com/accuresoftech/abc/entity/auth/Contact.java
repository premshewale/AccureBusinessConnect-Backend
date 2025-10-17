package com.accuresoftech.abc.entity.auth;

import com.accuresoftech.abc.entity.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false; 

    public enum Role {
        PRIMARY, BILLING, TECHNICAL
    }

    public Contact() {
        // JPA requires a default constructor
    }

    //Constructor

    public Contact(Long id, Customer customer, String firstName, String lastName, String email, String phone, Role role, boolean isPrimary) {
        this.id = id;
        this.customer = customer;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isPrimary = isPrimary;
    }

    // Getters and Setters


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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
