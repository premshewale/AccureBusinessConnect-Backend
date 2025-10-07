package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.entity.auth.Department;
import java.util.List;

public interface DepartmentService {
    DepartmentResponse create(Department d);
    DepartmentResponse update(Long id, Department d);
    DepartmentResponse getById(Long id);
    List<DepartmentResponse> getAll();
    void delete(Long id);
}
