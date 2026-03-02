package com.store.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.dto.SimpleAuthResponse;
import com.store.backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
		authService.register(request);
		return ResponseEntity.ok("User Registered Successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<SimpleAuthResponse> login(@RequestBody LoginRequest request) {
		SimpleAuthResponse response = authService.loginSimple(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		// In a real session-based system, you would invalidate the session here
		return ResponseEntity.ok("Logged out successfully");
	}
}
