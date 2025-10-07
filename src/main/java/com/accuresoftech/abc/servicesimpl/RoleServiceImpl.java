package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.RoleRepository;
import com.accuresoftech.abc.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role getByKey(RoleKey key) {
		return roleRepository.findByKey(key).orElseThrow(() -> new ResourceNotFoundException("Role not found: " + key));
	}
}
