package com.accuresoftech.abc.services;

import com.accuresoftech.abc.entity.auth.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department d);
    Department update(Long id, Department d);
    Department getById(Long id);
    List<Department> getAll();
    void delete(Long id);
}
