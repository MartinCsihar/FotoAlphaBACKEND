package com.fotoalpha.awsservice.Configuration.SecurityService;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public <T> T extractClaim(String token, Function<Claims, T> parser) {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return parser.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token){
        try {
            extractUsername(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
