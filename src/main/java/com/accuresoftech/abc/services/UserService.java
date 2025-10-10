package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse create(RegisterUserRequest req);
    UserResponse createAdmin(RegisterUserRequest req);
    UserResponse update(Long id, UpdateUserRequest req);
    void delete(Long id);
    List<UserResponse> getAll();
    UserResponse getById(Long id);

    // helpers
    List<UserResponse> getDepartmentUsers(Long departmentId);
    UserResponse getMyProfile(String email);
}
