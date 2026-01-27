package com.accuresoftech.abc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByDeletedFalse();

    @Query("""
        SELECT p FROM Project p
        WHERE p.customer.department.id = :departmentId
        AND p.deleted = false
    """)
    List<Project> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("""
        SELECT p FROM Project p
        JOIN p.members m
        WHERE m.id = :userId
        AND p.deleted = false
    """)
    List<Project> findByMemberId(@Param("userId") Long userId);
}