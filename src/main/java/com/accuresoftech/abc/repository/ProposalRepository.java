package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Proposal;
import com.accuresoftech.abc.enums.ProposalStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> 
{
    List<Proposal> findByCustomerId(Long customerId);
    
    @Query("SELECT p FROM Proposal p WHERE p.department.id = :departmentId")
    List<Proposal> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    
    List<Proposal> findByOwnerId(Long ownerId);
    
   
    
   
    List<Proposal> findByStatus(ProposalStatus status);
    
    @Query("SELECT p FROM Proposal p WHERE p.status = :status AND p.department.id = :departmentId")
    List<Proposal> findByStatusAndDepartment(@Param("status") ProposalStatus status, 
                                           @Param("departmentId") Long departmentId);
    
    @Query("SELECT COUNT(p) FROM Proposal p WHERE p.customer.id = :customerId")
    Long countByCustomerId(@Param("customerId") Long customerId);

    
   
    List<Proposal> findByDeletedFalse();
    List<Proposal> findByOwnerIdAndDeletedFalse(Long ownerId);
    List<Proposal> findByDepartmentIdAndDeletedFalse(Long departmentId);
    List<Proposal> findByCustomerIdAndDeletedFalse(Long customerId);
    List<Proposal> findByStatusAndDeletedFalse(ProposalStatus status);
  //  List<Proposal> findByStatusAndDepartmentAndDeletedFalse(ProposalStatus status, Long departmentId);
    List<Proposal> findByStatusAndDepartment_IdAndDeletedFalse(ProposalStatus status, Long departmentId);
    
}