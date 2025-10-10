package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.request.RegisterUserRequest;
import com.accuresoftech.abc.dto.request.UpdateUserRequest;
import com.accuresoftech.abc.dto.response.UserResponse;
import com.accuresoftech.abc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create user (Admin or SubAdmin)
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody RegisterUserRequest req) {
        return ResponseEntity.ok(userService.create(req));
    }

    // Update user (Admin/SubAdmin/Staff with rules)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UpdateUserRequest req) {
        return ResponseEntity.ok(userService.update(id, req));
    }

    // List users - filtered by role (Admin gets all, SubAdmin gets department, Staff gets self)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    // Get by id - permission checked in service
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Delete - admin only (checked in service)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Profile shortcut
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        
        List<UserResponse> list = userService.getAll();
        // For staff getAll returns only self; for admin returns all 
        return ResponseEntity.ok(list.get(0));
    }
}
