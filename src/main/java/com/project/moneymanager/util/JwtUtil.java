package com.project.moneymanager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Inject the Base64-encoded secret from application.properties
    @Value("${jwt.secret}")
    private String secretBase64;

    // Use a private final Key field to initialize the key once. This avoids repeated
    // key generation (and potential encoding issues) on every request.
    private final Key signingKey;

    // Constructor to initialize the key immediately upon component creation
    public JwtUtil(@Value("${jwt.secret}") String secretBase64) {
        // Decode the Base64 string into a byte array
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);

        // Use the byte array directly with Keys.hmacShaKeyFor
        // The original issue was likely caused by the jjwt implementation
        // expecting a raw, high-entropy key, which a simple string sometimes isn't.
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // NOTE: We no longer need getSigningKey() as the Key is initialized once.

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                // 1000 * 60 * 60 * 10 = 10 hours
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(this.signingKey, SignatureAlgorithm.HS256) // Use the pre-initialized key
                .compact();
    }

    private Claims extractAllClaims(String token) {
        // Validation logic uses the pre-initialized key
        return Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !extractAllClaims(token).getExpiration().before(new Date());
    }
}
