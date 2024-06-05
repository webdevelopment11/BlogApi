package com.springboot.Blog.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.springboot.Blog.exception.BlogApiException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app-jwt-expiration-milliseconds}")
	private long jwtExpirationDate;
	
	public String generateToken(Authentication authentication) {
		String username=authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime()+jwtExpirationDate);
		
		String token = Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(expireDate)
				.signWith(key())
				.compact();
		
		return token;
	}
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUsername(String token) {
		return Jwts.parser()
			.verifyWith((SecretKey) key())
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}
	
	
	public boolean validateToken(String token) {
		
		try {
			Jwts.parser()
				.verifyWith((SecretKey) key())
				.build()
				.parse(token);
		
			return true;
		}catch(MalformedJwtException malformedJwtException) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
		}
		catch(ExpiredJwtException expiredJwtException) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
		}catch(UnsupportedJwtException unsupportedException) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
		}catch(IllegalArgumentException illegalArgumentException) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
		}
		
	}
	
}
