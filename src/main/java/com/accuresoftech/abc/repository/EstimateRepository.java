package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    List<Estimate> findByDepartmentId(Long departmentId);
    List<Estimate> findByAssignedToId(Long userId);
    List<Estimate> findByCustomerId(Long customerId);
}
