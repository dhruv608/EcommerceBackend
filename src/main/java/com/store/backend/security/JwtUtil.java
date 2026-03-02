package com.store.backend.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
	private static final String SECRET_KEY =
	        "store-access-key-store-access-key-store-access-key";
  private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  private final long EXPIRATION_TIME = 15*60*1000;
  
  public String generateToken(Long userId, String email,String role)
  {
	  return Jwts.builder()
              .setSubject(email)
              .claim("userId", userId)
              .claim("role", role)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
              .signWith(key,SignatureAlgorithm.HS256)
              .compact();
  }
  
  public Claims extractAllClaims(String token) {
      return Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();
  }
  
  public boolean isTokenExpired(String token) {
      return extractAllClaims(token)
              .getExpiration()
              .before(new Date());
  }
  
  public String extractEmail(String token) {
      return extractAllClaims(token).getSubject();
  }
  
}
