package com.accuresoftech.abc.utils;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.dto.response.RoleResponse;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.entity.auth.User;

// for auth entities only
public class EntityMapper
{

    public static UserResponse toUserResponse(User user)
    {
        if (user == null) {
			return null;
		}
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .jobTitle(user.getJobTitle())
                .phoneExtension(user.getPhoneExtension())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .build();
    }

    public static DepartmentResponse toDepartmentResponse(Department d) {
        if (d == null) {
			return null;
		}
        return DepartmentResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .managerName(d.getManager() != null ? d.getManager().getName() : null)
                .build();
    }

    public static RoleResponse toRoleResponse(Role r)
    {
        if (r == null) {
			return null;
		}
        return RoleResponse.builder()
                .id(r.getId())
                .key(r.getKey().name())
                .name(r.getName())
                .build();
    }
}
