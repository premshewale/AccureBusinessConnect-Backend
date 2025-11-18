package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.enums.RoleKey;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findByDepartmentId(Long departmentId);
	
	
	//Optional<User> findFirstByRole(RoleKey role);
	Optional<User> findFirstByRole_Key(RoleKey key);


}
