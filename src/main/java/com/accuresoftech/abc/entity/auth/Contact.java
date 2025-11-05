package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import com.accuresoftech.abc.enums.ContactRole;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private ContactRole role;   // PRIMARY, BILLING, TECHNICAL

    private boolean isPrimary;

    private LocalDateTime createdAt = LocalDateTime.now(); 
    
    
    
}