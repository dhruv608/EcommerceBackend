package com.store.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 🔐 Login related errors
	@ExceptionHandler({ UserNotFoundException.class, InvalidCredentialsException.class })
	public ResponseEntity<?> handleAuthExceptions(RuntimeException ex) {

		// ❗ Frontend ko SAME message (security reason)
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or password"));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntime(RuntimeException ex) {

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(CategoryDeleteException.class)
	public ResponseEntity<Map<String, Object>> handleCategoryDeleteException(CategoryDeleteException ex) {

		Map<String, Object> error = new HashMap<>();
		error.put("message", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.value());
		error.put("timestamp", LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	// 🧨 Fallback for any unexpected error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAll(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Something went wrong"));
	}
}
