package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.RoleRepository;
import com.accuresoftech.abc.repository.UserRepository;

import com.accuresoftech.abc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.response.UserResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User create(RegisterUserRequest req) {
		Role role = roleRepository.findByKey(req.getRoleKey() == null ? RoleKey.STAFF : req.getRoleKey())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		Department dept = null;
		if (req.getDepartmentId() != null) {
			dept = departmentRepository.findById(req.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		}

		User u = User.builder().name(req.getName()).email(req.getEmail())
				.passwordHash(passwordEncoder.encode(req.getPassword())).jobTitle(req.getJobTitle()).role(role)
				.department(dept).status(com.accuresoftech.abc.enums.UserStatus.ACTIVE).build();

		return userRepository.save(u);
	}

	@Override
	public User update(Long id, UpdateUserRequest req) {
		User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (req.getName() != null)
			u.setName(req.getName());
		if (req.getJobTitle() != null)
			u.setJobTitle(req.getJobTitle());
		if (req.getPhoneExtension() != null)
			u.setPhoneExtension(req.getPhoneExtension());
		if (req.getStatus() != null)
			u.setStatus(req.getStatus());
		if (req.getDepartmentId() != null) {
			Department d = departmentRepository.findById(req.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
			u.setDepartment(d);
		}
		return userRepository.save(u);
	}

	@Override
	public void delete(Long id) {
		// soft delete
		User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		u.setDeleted(true);
		userRepository.save(u);
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll().stream().filter(u -> !u.isDeleted()).toList();
	}

	@Override
	public User getById(Long id) {
		User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		if (u.isDeleted())
			throw new ResourceNotFoundException("User not found");
		return u;
	}

	@Override
	public User createAdmin(RegisterUserRequest req) {
	    // Always assign ADMIN role
	    Role role = roleRepository.findByKey(RoleKey.ADMIN)
	            .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

	    Department dept = null;
	    if (req.getDepartmentId() != null) {
	        dept = departmentRepository.findById(req.getDepartmentId())
	                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
	    }

	    User u = User.builder()
	            .name(req.getName())
	            .email(req.getEmail())
	            .passwordHash(passwordEncoder.encode(req.getPassword()))
	            .jobTitle(req.getJobTitle())
	            .role(role)
	            .department(dept)
	            .status(com.accuresoftech.abc.enums.UserStatus.ACTIVE)
	            .build();

	    return userRepository.save(u);
	}

	
	
}
