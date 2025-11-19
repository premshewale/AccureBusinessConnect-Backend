package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.response.RoleResponse;
import com.accuresoftech.abc.services.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@GetMapping
	public ResponseEntity<List<RoleResponse>> getAll() {
		return ResponseEntity.ok(roleService.getAllRoles());
	}

	@GetMapping("/{key}")
	public ResponseEntity<RoleResponse> getByKey(@PathVariable String key) {
		return ResponseEntity.ok(roleService.getByKey(key));
	}
}
