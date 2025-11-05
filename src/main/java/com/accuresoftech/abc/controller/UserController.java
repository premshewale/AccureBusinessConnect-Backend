package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.dto.response.ApiResponse;
import com.accuresoftech.abc.services.UserService;
import com.accuresoftech.abc.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController 
{

	private final UserService userService;
	private final AuthUtils authUtils;

	// Create user (Admin or SubAdmin)
	@PostMapping
	public ResponseEntity<UserResponse> create(@Valid @RequestBody RegisterUserRequest req) {
		return ResponseEntity.ok(userService.create(req));
	}

	// Update user (Admin/SubAdmin/Staff with rules)
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest req) {
		return ResponseEntity.ok(userService.update(id, req));
	}

	// List users - filtered by role (Admin gets all, SubAdmin gets department,
	// Staff gets self)
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAll() {
		return ResponseEntity.ok(userService.getAll());
	}

	// Get by id - permission checked in service
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getById(id));
	}

	// Activate / Deactivate endpoints
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
		UserResponse res = userService.deactivateUser(id);
		
		return ResponseEntity.ok(res);
	}

	
	@PutMapping("/{id}/activate")
	public ResponseEntity<UserResponse> activateUser(@PathVariable Long id) {
		UserResponse res = userService.activateUser(id);
		return ResponseEntity.ok(res);
	}

	// Profile
	@GetMapping("/me")
	public ResponseEntity<UserResponse> me() {
		var current = authUtils.getCurrentUser();
		if (current == null) {
			throw new AccessDeniedException("Unauthorized");
		}
		UserResponse profile = userService.getMyProfile(current.getEmail());
		return ResponseEntity.ok(profile);
	}
}
