package com.accuresoftech.abc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accuresoftech.abc.dto.response.DepartmentResponse;
import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.services.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@PostMapping
	public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody Department dept)
	{
		return ResponseEntity.ok(departmentService.create(dept));
	}

	@PutMapping("/{id}")
	public ResponseEntity<DepartmentResponse> update(@PathVariable Long id, @Valid @RequestBody Department dept) {
		return ResponseEntity.ok(departmentService.update(id, dept));
	}

	@GetMapping
	public ResponseEntity<List<DepartmentResponse>> getAll() {
		return ResponseEntity.ok(departmentService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(departmentService.getById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		departmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
