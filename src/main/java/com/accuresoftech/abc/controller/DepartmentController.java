package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Department> create(@Valid @RequestBody Department dept) {
        return ResponseEntity.ok(departmentService.create(dept));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id, @Valid @RequestBody Department dept) {
        return ResponseEntity.ok(departmentService.update(id, dept));
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
