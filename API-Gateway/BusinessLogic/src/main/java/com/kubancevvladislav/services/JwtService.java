package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {
    private String secret;
    private int expiresIn;

    public JwtService (
            @Value("${secret}") String secret,
            @Value("${expirationMS}") int expiresIn
    ) {
        this.secret = secret;
        this.expiresIn = expiresIn;
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        List<String> roles = role.stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        JwtParserBuilder parser = Jwts.parser();
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        parser.verifyWith(key);

        return parser.build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        return Role.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isAccessTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        return username.equals(user.getUsername()) && isAccessTokenNotExpired(token);
    }
}
