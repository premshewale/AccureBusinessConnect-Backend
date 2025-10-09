package com.accuresoftech.abc.repository;

import com.accuresoftech.abc.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
