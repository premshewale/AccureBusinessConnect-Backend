package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.enums.UserStatus;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.repository.RoleRepository;
import com.accuresoftech.abc.repository.UserRepository;
import com.accuresoftech.abc.services.UserService;
import com.accuresoftech.abc.utils.AuthUtils;
import com.accuresoftech.abc.utils.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

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
        User current = authUtils.getCurrentUser();
        if (current == null) {
            throw new AccessDeniedException("Unauthorized");
        }

        // Admin can create any user. SubAdmin can only create STAFF under their department.
        if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
            // force staff role and set department
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

        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .jobTitle(req.getJobTitle())
                .role(role)
                .department(dept)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(u);
        return EntityMapper.toUserResponse(u);
    }

    @Override
    public UserResponse createAdmin(RegisterUserRequest req) {
        // register-admin endpoint (public) — creates ADMIN user
        Role role = roleRepository.findByKey(RoleKey.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        User u = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .jobTitle(req.getJobTitle())
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(u);
        return EntityMapper.toUserResponse(u);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest req) {
        User current = authUtils.getCurrentUser();
        if (current == null) throw new AccessDeniedException("Unauthorized");

        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Permission checks:
        // - Admin can update any
        // - SubAdmin can update only users in their department
        // - Staff can update only themselves
        if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
            if (u.getDepartment() == null || current.getDepartment() == null ||
                    !u.getDepartment().getId().equals(current.getDepartment().getId())) {
                throw new AccessDeniedException("Manager can update only users in their department");
            }
        } else if (current.getRole().getKey() == RoleKey.STAFF) {
            if (!current.getId().equals(u.getId())) {
                throw new AccessDeniedException("Staff can update only their own profile");
            }
        }

        if (req.getName() != null) u.setName(req.getName());
        if (req.getJobTitle() != null) u.setJobTitle(req.getJobTitle());
        if (req.getPhoneExtension() != null) u.setPhoneExtension(req.getPhoneExtension());
        if (req.getStatus() != null) u.setStatus(req.getStatus());
        if (req.getDepartmentId() != null) {
            Department d = departmentRepository.findById(req.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            u.setDepartment(d);
        }

        userRepository.save(u);
        return EntityMapper.toUserResponse(u);
    }

    @Override
    public void delete(Long id) {
        User current = authUtils.getCurrentUser();
        if (current == null) throw new AccessDeniedException("Unauthorized");
        if (current.getRole().getKey() != RoleKey.ADMIN) {
            throw new AccessDeniedException("Only Admin can delete users");
        }

        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        u.setDeleted(true);
        userRepository.save(u);
    }

    @Override
    public List<UserResponse> getAll() {
        User current = authUtils.getCurrentUser();
        if (current == null) throw new AccessDeniedException("Unauthorized");

        if (current.getRole().getKey() == RoleKey.ADMIN) {
            return userRepository.findAll().stream()
                    .filter(u -> !u.isDeleted())
                    .map(EntityMapper::toUserResponse)
                    .toList();
        } else if (current.getRole().getKey() == RoleKey.SUB_ADMIN) {
            Long deptId = current.getDepartment() != null ? current.getDepartment().getId() : null;
            if (deptId == null) return List.of();
            return userRepository.findByDepartmentId(deptId).stream()
                    .filter(u -> !u.isDeleted())
                    .map(EntityMapper::toUserResponse)
                    .toList();
        } else {
            // Staff -> only themselves
            return List.of(EntityMapper.toUserResponse(current));
        }
    }

    @Override
    public UserResponse getById(Long id) {
        User current = authUtils.getCurrentUser();
        if (current == null) throw new AccessDeniedException("Unauthorized");

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
                .filter(u -> !u.isDeleted())
                .map(EntityMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getMyProfile(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return EntityMapper.toUserResponse(u);
    }
}
