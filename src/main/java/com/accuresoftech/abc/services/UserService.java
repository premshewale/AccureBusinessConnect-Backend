package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;

public interface UserService {
	UserResponse create(RegisterUserRequest req);

	UserResponse createAdmin(RegisterUserRequest req);

	UserResponse update(Long id, UpdateUserRequest req);

	List<UserResponse> getAll();

	UserResponse getById(Long id);

	// helpers
	List<UserResponse> getDepartmentUsers(Long departmentId);

	UserResponse getMyProfile(String email);

	// activation / deactivation
	UserResponse deactivateUser(Long id);

	UserResponse activateUser(Long id);
}
