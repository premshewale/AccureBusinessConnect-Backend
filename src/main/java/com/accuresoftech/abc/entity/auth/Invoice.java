package com.accuresoftech.abc.entity.auth; 
import com.accuresoftech.abc.entity.BaseEntity; 
import com.accuresoftech.abc.enums.InvoiceStatus; 
import jakarta.persistence.*; 
import lombok.*; 
import java.math.BigDecimal; 
import java.time.LocalDate; 
import java.util.List; 
@Entity 
@Table(name = "invoices") 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class Invoice extends BaseEntity { 
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY) 
private Long id; 
// optional link to a proposal (if invoice was created from a proposal) 
@ManyToOne(fetch = FetchType.LAZY) 
@JoinColumn(name = "proposal_id") 
private Proposal proposal; 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "customer_id", nullable = false) 
    private Customer customer; 
 
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false) 
    private InvoiceStatus status;   // DRAFT, SENT, PAID, UNPAID, OVERDUE, CANCELLED 
 
    @Column(nullable = false) 
    private LocalDate issueDate; 
 
    @Column(nullable = false) 
    private LocalDate dueDate; 
 
    @Column(nullable = false, precision = 19, scale = 2) 
    private BigDecimal totalAmount; 
 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "department_id") 
    private Department department; 
 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "created_by") 
    private User createdBy; 
 
    // payments (optional, if you have Payment entity) 
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = 
true) 
    private List<Payment> payments; 
}