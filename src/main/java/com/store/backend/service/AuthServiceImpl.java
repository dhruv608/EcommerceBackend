package com.store.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.dto.SimpleAuthResponse;
import com.store.backend.entity.User;
import com.store.backend.exception.InvalidCredentialsException;
import com.store.backend.exception.UserNotFoundException;
import com.store.backend.repository.UserRepository;
import com.store.backend.security.JwtUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtill;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtill) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtill = jwtUtill;
	}

	@Override
	public void register(RegisterRequest request) {
		// TODO Auto-generated method stub
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email Already Registered");
		}
		String hashedPassword = passwordEncoder.encode(request.getPassword());

		User user = new User();

		user.setName(request.getName());

		user.setEmail(request.getEmail());

		user.setPassword(hashedPassword);

		user.setRole("USER");
		user.setIsActive(true);
		userRepository.save(user);

	}

	@Override
	public String login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User does not exist"));
		boolean passwordMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!passwordMatched) {
			throw new InvalidCredentialsException("Invalid Email or password");
		}
		return jwtUtill.generateToken(user.getId(), user.getEmail(), user.getRole());

	}

	@Override
	public SimpleAuthResponse loginSimple(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException("User does not exist"));
		boolean passwordMatched = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!passwordMatched) {
			throw new InvalidCredentialsException("Invalid Email or password");
		}

		// Return simple user info without JWT token
		return new SimpleAuthResponse(
				"Login successful",
				user.getId().toString(),
				user.getName(),
				user.getEmail(),
				user.getRole());
	}

}
