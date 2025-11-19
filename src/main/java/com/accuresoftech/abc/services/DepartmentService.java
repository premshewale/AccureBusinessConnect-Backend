package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.entity.auth.Department;

public interface DepartmentService {
	DepartmentResponse create(Department d);

	DepartmentResponse update(Long id, Department d);

	DepartmentResponse getById(Long id);

	List<DepartmentResponse> getAll();

	void delete(Long id);
}
