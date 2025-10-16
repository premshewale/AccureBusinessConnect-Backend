package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.services.DepartmentService;
import com.accuresoftech.abc.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;

	@Override
	public DepartmentResponse create(Department d) {
		Department saved = departmentRepository.save(d);
		return EntityMapper.toDepartmentResponse(saved);
	}

	@Override
	public DepartmentResponse update(Long id, Department d) {
		Department existing = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		existing.setName(d.getName());
		existing.setManager(d.getManager());
		departmentRepository.save(existing);
		return EntityMapper.toDepartmentResponse(existing);
	}

	@Override
	public DepartmentResponse getById(Long id) {
		Department dept = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		return EntityMapper.toDepartmentResponse(dept);
	}

	@Override
	public List<DepartmentResponse> getAll() {
		return departmentRepository.findAll().stream().map(EntityMapper::toDepartmentResponse).toList();
	}

	@Override
	public void delete(Long id) {
		departmentRepository.deleteById(id);
	}
}
