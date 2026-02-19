package com.accuresoftech.abc.entity.auth;

import java.time.LocalDate;

import com.accuresoftech.abc.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_milestones")
public class Milestone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate dueDate;

    private Boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}