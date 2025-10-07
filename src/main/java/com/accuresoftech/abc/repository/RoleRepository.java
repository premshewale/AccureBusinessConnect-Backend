package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.Role;
import com.accuresoftech.abc.enums.RoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByKey(RoleKey key);
}
