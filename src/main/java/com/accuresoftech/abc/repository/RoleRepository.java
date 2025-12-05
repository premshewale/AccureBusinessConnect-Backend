package com.accuresoftech.abc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.enums.RoleKey;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByKey(RoleKey key);
}
