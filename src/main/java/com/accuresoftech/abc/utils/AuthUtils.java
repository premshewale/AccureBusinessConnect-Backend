package com.accuresoftech.abc.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.accuresoftech.abc.entity.auth.User;
import com.accuresoftech.abc.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtils {

	private final UserRepository userRepository;

	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getName() == null) {
			return null;
		}
		return userRepository.findByEmail(auth.getName())
				.orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
	}
}
