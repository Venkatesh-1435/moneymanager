package com.money.manager.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	private final SecretKey key;

    public JwtUtils(@Value("${jwt.secret.key}") String secretKeyFromConfig) {
        this.key = Keys.hmacShaKeyFor(secretKeyFromConfig.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean validateToken(String token, String email) {
        Claims claims = getClaims(token);
        String claimEmail = claims.getSubject();
        boolean expired = isTokenExpired(token);
        return email.equals(claimEmail) && !expired;
    }

    public boolean isTokenExpired(String token) {
        Claims claims = getClaims(token);
        Date date = claims.getExpiration();
        return date.before(new Date());
    }


}
