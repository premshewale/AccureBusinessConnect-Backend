package com.accuresoftech.abc.entity.auth;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.accuresoftech.abc.entity.BaseEntity;
import com.accuresoftech.abc.enums.BillingType;
import com.accuresoftech.abc.enums.ProjectStatus;
import com.accuresoftech.abc.enums.ProposalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingType billingType;

    private BigDecimal ratePerHour;
    private Integer estimatedHours;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate deadline;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "project_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "project_tags",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<ProjectTag> tags;

    @Column(columnDefinition = "TEXT")
    private String description;
}