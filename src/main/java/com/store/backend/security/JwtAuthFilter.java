package com.store.backend.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
	private final JwtUtil jwtUtil;

	public JwtAuthFilter(JwtUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		  if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
		        response.setStatus(HttpServletResponse.SC_OK);
		        filterChain.doFilter(request, response);
		        return;
		    }
		
		  String authHeader = request.getHeader("Authorization");
		  if (authHeader != null && authHeader.startsWith("Bearer ")) {

			  String token = authHeader.substring(7);

	            try {
	                if (!jwtUtil.isTokenExpired(token)) {

	                    String email = jwtUtil.extractEmail(token);

	                    UsernamePasswordAuthenticationToken authentication =
	                            new UsernamePasswordAuthenticationToken(
	                                    email,
	                                    null,
	                                    Collections.emptyList()
	                            );

	                    SecurityContextHolder.getContext()
	                            .setAuthentication(authentication);
	                }
	            } catch (Exception e) {
	                // invalid token → ignore, will result in 401
	            }
		  }
		  filterChain.doFilter(request, response);
		  
		  
	}
	
}
