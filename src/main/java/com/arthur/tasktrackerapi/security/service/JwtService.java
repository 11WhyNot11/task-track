package com.arthur.tasktrackerapi.security.service;

import com.arthur.tasktrackerapi.user.entity.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "super-secure-secret-key-which-is-at-least-32-chars";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token){
        var parser = Jwts.parserBuilder().
                setSigningKey(getSigningKey())
                .build();

        return parser.parseClaimsJws(token).getBody().getSubject();
    }

    public Date extractExpiration(String token){
        var parser = Jwts.parserBuilder().
                setSigningKey(getSigningKey())
                .build();

        return parser.parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && extractExpiration(token).after(new Date());
    }

    private Key getSigningKey(){
        var bytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
