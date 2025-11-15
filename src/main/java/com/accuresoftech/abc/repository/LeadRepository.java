package com.accuresoftech.abc.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accuresoftech.abc.entity.auth.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>
{
    List<Lead> findByOwnerId(Long ownerId);

    @Query("SELECT l FROM Lead l WHERE l.owner.department.id = :departmentId")
    List<Lead> findByDepartmentId(@Param("departmentId") Long departmentId);

    List<Lead> findByAssignedToId(Long userId);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}