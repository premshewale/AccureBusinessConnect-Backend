package com.accuresoftech.abc.entity.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "project_permissions")
public class ProjectPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private Boolean viewTasks;
    private Boolean createTasks;
    private Boolean editTasks;
    private Boolean commentOnTasks;

    private Boolean viewFiles;
    private Boolean uploadFiles;

    private Boolean viewTimesheets;
    private Boolean viewGantt;

    private Boolean viewFinance;
}