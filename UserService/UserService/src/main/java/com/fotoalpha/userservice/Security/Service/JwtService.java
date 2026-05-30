package com.fotoalpha.userservice.Security.Service;

import com.fotoalpha.userservice.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public <T> T extractClaim(String token, Function<Claims, T> parser) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return parser.apply(claims);

    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
            return extractClaim(token, claims -> claims.get("role", String.class));
    }
    public boolean isTokenValid(String token){
        try {
            extractUsername(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUserID())
                .claim("role", user.getRole())
                .signWith(getSigningKey())
                .compact();
    }
}
