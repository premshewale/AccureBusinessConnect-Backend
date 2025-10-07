package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
