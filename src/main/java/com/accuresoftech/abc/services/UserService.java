package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.dto.response.UserResponse;

import java.util.List;

public interface UserService {
	User createAdmin(RegisterUserRequest req);

    User create(RegisterUserRequest req);
    User update(Long id, UpdateUserRequest req);
    void delete(Long id);
    List<User> getAll();
    User getById(Long id);
}
