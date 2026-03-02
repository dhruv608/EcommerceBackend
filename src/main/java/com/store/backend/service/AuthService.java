package com.store.backend.service;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.dto.SimpleAuthResponse;

public interface AuthService {
	void register(RegisterRequest request);

	String login(LoginRequest request);

	SimpleAuthResponse loginSimple(LoginRequest request);
}
