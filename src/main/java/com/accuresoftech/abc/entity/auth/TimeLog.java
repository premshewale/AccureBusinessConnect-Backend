package com.accuresoftech.abc.entity.auth;



import java.time.LocalDate;

import com.accuresoftech.abc.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "project_time_logs")
public class TimeLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate workDate;
    private Integer hours;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private ProjectTask task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}