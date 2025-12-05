package com.accuresoftech.abc.services;

import java.util.List;

import com.accuresoftech.abc.dto.response.RoleResponse;

public interface RoleService {
	List<RoleResponse> getAllRoles();

	RoleResponse getByKey(String key);
}
