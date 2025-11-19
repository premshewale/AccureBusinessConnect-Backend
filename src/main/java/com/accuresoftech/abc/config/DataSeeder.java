package com.accuresoftech.abc.config;

import org.springframework.stereotype.Component;

import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.enums.RoleKey;
import com.accuresoftech.abc.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder {

	private final RoleRepository roleRepository;

	@PostConstruct
	public void init() {
		for (RoleKey key : RoleKey.values()) {
			roleRepository.findByKey(key).orElseGet(() -> {
				Role r = Role.builder().key(key)
						.name(key.name().charAt(0) + key.name().substring(1).toLowerCase().replace("_", " ")).build();
				return roleRepository.save(r);
			});
		}
	}
}
