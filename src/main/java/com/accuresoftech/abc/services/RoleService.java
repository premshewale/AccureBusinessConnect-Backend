package com.accuresoftech.abc.services;

import com.accuresoftech.abc.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {
	List<RoleResponse> getAllRoles();

	RoleResponse getByKey(String key);
}
