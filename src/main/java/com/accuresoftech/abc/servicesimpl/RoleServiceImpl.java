package com.accuresoftech.abc.servicesimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accuresoftech.abc.dto.response.RoleResponse;
import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.RoleRepository;
import com.accuresoftech.abc.services.RoleService;
import com.accuresoftech.abc.utils.EntityMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public List<RoleResponse> getAllRoles() {
		return roleRepository.findAll().stream().map(EntityMapper::toRoleResponse).toList();
	}

	@Override
	public RoleResponse getByKey(String key) {
		RoleKey roleKey;
		try {
			roleKey = RoleKey.valueOf(key.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException("Invalid role key: " + key);
		}
		Role r = roleRepository.findByKey(roleKey)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + key));
		return EntityMapper.toRoleResponse(r);
	}
}
