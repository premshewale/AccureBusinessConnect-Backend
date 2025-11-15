package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.BadRequestException;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.DepartmentService;
import com.accuresoftech.abc.utils.EntityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final UserRepository userRepository;

	@Override
	public DepartmentResponse create(Department d) {
		// name uniqueness
		if (d.getName() == null || d.getName().trim().isEmpty()) {
			throw new BadRequestException("Department name is required");
		}
		Optional<Department> existing = departmentRepository.findByName(d.getName().trim());
		if (existing.isPresent()) {
			throw new BadRequestException("Department with this name already exists");
		}

		// validate manager if provided
		if (d.getManager() != null) {
			Long managerId = d.getManager().getId();
			if (managerId == null) {
				throw new BadRequestException("Manager id is required when manager is provided");
			}
			User manager = userRepository.findById(managerId)
					.orElseThrow(() -> new ResourceNotFoundException("Manager user not found: " + managerId));
			if (manager.getRole() == null || manager.getRole().getKey() != RoleKey.SUB_ADMIN) {
				throw new BadRequestException("Manager must have SUB_ADMIN role");
			}
			d.setManager(manager); // ensure managed entity is attached
		}

		Department saved = departmentRepository.save(d);
		return EntityMapper.toDepartmentResponse(saved);
	}

	@Override
	public DepartmentResponse update(Long id, Department d) {
		Department existing = departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found"));

		// name change -> uniqueness check
		if (d.getName() != null && !d.getName().trim().isEmpty()
				&& !d.getName().trim().equalsIgnoreCase(existing.getName())) {
			Optional<Department> other = departmentRepository.findByName(d.getName().trim());
			if (other.isPresent() && !other.get().getId().equals(id)) {
				throw new BadRequestException("Another department with this name already exists");
			}
			existing.setName(d.getName().trim());
		}

		// manager update => validate role
		if (d.getManager() != null) {
			Long managerId = d.getManager().getId();
			if (managerId == null) {
				throw new BadRequestException("Manager id is required when manager is provided");
			}
			User manager = userRepository.findById(managerId)
					.orElseThrow(() -> new ResourceNotFoundException("Manager user not found: " + managerId));
			if (manager.getRole() == null || manager.getRole().getKey() != RoleKey.SUB_ADMIN) {
				throw new BadRequestException("Manager must have SUB_ADMIN role");
			}
			existing.setManager(manager);
		} else {
			// allow clearing manager if client explicitly sends null (no-op if unchanged)
			existing.setManager(null);
		}

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
		// consider soft-delete later; for now keep as-is
		departmentRepository.deleteById(id);
	}
}
