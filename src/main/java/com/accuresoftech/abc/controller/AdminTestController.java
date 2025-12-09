package com.accuresoftech.abc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/test")
public class AdminTestController {

	@GetMapping("/ping")
	public ResponseEntity<?> ping() {
		return ResponseEntity.ok("Admin Module Active and Authenticated");
	}

	@GetMapping("/secure")
	public ResponseEntity<?> securedEndpoint() {
		return ResponseEntity.ok("Secure Admin API Access Confirmed");
	}
}
