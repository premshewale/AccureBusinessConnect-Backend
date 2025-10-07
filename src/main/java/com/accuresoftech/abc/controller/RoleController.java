package com.accuresoftech.abc.controller;

import com.accuresoftech.abc.dto.response.RoleResponse;
import com.accuresoftech.abc.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
