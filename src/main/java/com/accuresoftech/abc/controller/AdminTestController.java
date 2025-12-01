package com.accuresoftech.abc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/test")
public class AdminTestController {

	@GetMapping("	")
	public ResponseEntity<?> ping() {
		return ResponseEntity.ok("Admin Module Active and Authenticated");
	}

	@GetMapping("/secure")
	public ResponseEntity<?> securedEndpoint() {
		return ResponseEntity.ok("Secure Admin API Access Confirmed");
	}
}
