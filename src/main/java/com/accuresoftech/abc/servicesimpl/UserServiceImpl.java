package com.accuresoftech.abc.servicesimpl;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.UserStatus;
import com.accuresoftech.abc.exception.BadRequestException;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.RoleRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.UserService;
import com.accuresoftech.abc.utils.AuthUtils;
import com.accuresoftech.abc.utils.EntityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final DepartmentRepository departmentRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthUtils authUtils;

	@Override
	public UserResponse create(RegisterUserRequest req) {
		var current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		// Admin can create any user. SubAdmin can create only STAFF under their
		// department.
		if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			req.setRoleKey(RoleKey.STAFF);
			if (current.getDepartment() == null) {
				throw new ResourceNotFoundException("Manager has no department assigned");
			}
			req.setDepartmentId(current.getDepartment().getId());
		} else if (current.getRole().getKey() == RoleKey.STAFF) {
			throw new AccessDeniedException("Staff cannot create users");
		}

		Role role = roleRepository.findByKey(req.getRoleKey() == null ? RoleKey.STAFF : req.getRoleKey())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		Department dept = null;
		if (req.getDepartmentId() != null) {
			dept = departmentRepository.findById(req.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		}

		User u = User.builder().name(req.getName()).email(req.getEmail())
				.passwordHash(passwordEncoder.encode(req.getPassword())).jobTitle(req.getJobTitle()).role(role)
				.department(dept).status(UserStatus.ACTIVE).build();

		userRepository.save(u);

		// Assign as Department Manager if Sub-Admin
		if (u.getRole().getKey() == RoleKey.SUB_ADMIN && u.getDepartment() != null) {
			Department d = u.getDepartment();
			d.setManager(u);
			departmentRepository.save(d);
		}
		return EntityMapper.toUserResponse(u);
	}

	@Override
	public UserResponse createAdmin(RegisterUserRequest req) {
		// register-admin endpoint (public) — creates ADMIN user
		Role role = roleRepository.findByKey(RoleKey.ADMIN)
				.orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

		User u = User.builder().name(req.getName()).email(req.getEmail())
				.passwordHash(passwordEncoder.encode(req.getPassword())).jobTitle(req.getJobTitle()).role(role)
				.status(UserStatus.ACTIVE).build();

		userRepository.save(u);
		return EntityMapper.toUserResponse(u);
	}

	@Override
	public UserResponse update(Long id, UpdateUserRequest req) {
		var current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// Permission checks:
		if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			if (u.getDepartment() == null || current.getDepartment() == null
					|| !u.getDepartment().getId().equals(current.getDepartment().getId())) {
				throw new AccessDeniedException("Manager can update only users in their department");
			}
		} else if (current.getRole().getKey() == RoleKey.STAFF) {
			if (!current.getId().equals(u.getId())) {
				throw new AccessDeniedException("Staff can update only their own profile");
			}
		}

		if (req.getName() != null) {
			u.setName(req.getName());
		}
		if (req.getJobTitle() != null) {
			u.setJobTitle(req.getJobTitle());
		}
		if (req.getPhoneExtension() != null) {
			u.setPhoneExtension(req.getPhoneExtension());
		}
		if (req.getStatus() != null) {
			u.setStatus(req.getStatus());
		}
		if (req.getDepartmentId() != null) {
			Department d = departmentRepository.findById(req.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException("Department not found"));
			u.setDepartment(d);
		}

		userRepository.save(u);
		return EntityMapper.toUserResponse(u);
	}

	@Override
	public List<UserResponse> getAll() {
		var current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		if (current.getRole().getKey() == RoleKey.ADMIN) {
			return userRepository.findAll().stream().filter(u -> u.getStatus() != UserStatus.INACTIVE) // show active
					.map(EntityMapper::toUserResponse).toList();
		} else if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			Long deptId = current.getDepartment() != null ? current.getDepartment().getId() : null;
			if (deptId == null) {
				return List.of();
			}
			return userRepository.findByDepartmentId(deptId).stream().filter(u -> u.getStatus() != UserStatus.INACTIVE)
					.map(EntityMapper::toUserResponse).toList();
		} else {
			// Staff -> only themselves
			return List.of(EntityMapper.toUserResponse(current));
		}
	}

	@Override
	public UserResponse getById(Long id) {
		var current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (current.getRole().getKey() == RoleKey.ADMIN) {
			return EntityMapper.toUserResponse(u);
		} else if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			Long deptId = current.getDepartment() != null ? current.getDepartment().getId() : null;
			if (deptId == null || u.getDepartment() == null || !deptId.equals(u.getDepartment().getId())) {
				throw new AccessDeniedException("Manager can view only users in their department");
			}
			return EntityMapper.toUserResponse(u);
		} else {
			// staff can view only self
			if (!current.getId().equals(u.getId())) {
				throw new AccessDeniedException("Staff can view only own profile");
			}
			return EntityMapper.toUserResponse(u);
		}
	}

	@Override
	public List<UserResponse> getDepartmentUsers(Long departmentId) {
		return userRepository.findByDepartmentId(departmentId).stream()
				.filter(u -> u.getStatus() != UserStatus.INACTIVE).map(EntityMapper::toUserResponse).toList();
	}

	@Override
	public UserResponse getMyProfile(String email) {
		User u = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		return EntityMapper.toUserResponse(u);
	}

	@Override
	public UserResponse deactivateUser(Long id) {
		User current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		User target = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// Check access: Admin → all; SubAdmin → same department only
		if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			if (target.getDepartment() == null || current.getDepartment() == null
					|| !Objects.equals(current.getDepartment().getId(), target.getDepartment().getId())) {
				throw new AccessDeniedException("SubAdmin can deactivate users only in their own department");
			}
		} else if (current.getRole().getKey() != RoleKey.ADMIN) {
			throw new AccessDeniedException("Only Admin or SubAdmin can deactivate users");
		}

		if (target.getStatus() == UserStatus.INACTIVE) {
			throw new BadRequestException("User is already inactive");
		}

		target.setStatus(UserStatus.INACTIVE);
		userRepository.save(target);

		return EntityMapper.toUserResponse(target);
	}

	@Override
	public UserResponse activateUser(Long id) {
		User current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}

		User target = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// Check access: Admin → all; SubAdmin → same department only
		if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
			if (target.getDepartment() == null || current.getDepartment() == null
					|| !Objects.equals(current.getDepartment().getId(), target.getDepartment().getId())) {
				throw new AccessDeniedException("SubAdmin can activate users only in their own department");
			}
		} else if (current.getRole().getKey() != RoleKey.ADMIN) {
			throw new AccessDeniedException("Only Admin or SubAdmin can activate users");
		}

		if (target.getStatus() == UserStatus.ACTIVE) {
			throw new BadRequestException("User is already active");
		}

		target.setStatus(UserStatus.ACTIVE);
		userRepository.save(target);

		return EntityMapper.toUserResponse(target);
	}

}
