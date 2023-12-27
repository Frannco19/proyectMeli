package com.msmeli.configuration.security.service;

import com.msmeli.component.ScheduledTasks;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private long expirationTime;

    public String generateToken(String username, Long id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, id);
    }

    private String createToken(Map<String, Object> claims, String username, Long id) {
        return Jwts.builder()
                .setClaims(claims).setSubject(username+ "|" + id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) throws MalformedJwtException, SignatureException, ExpiredJwtException, UnsupportedJwtException {
        String username = null;
        Claims claims = extractAllClaims(token);

        String subject = claims.getSubject();
        if (subject != null) {
            String[] parts = subject.split("\\|");
            username = parts[0];
        } else {
            log.error("No se encontró el campo 'subject' en el token.");
        }
        return username;
    }
    public  Long extractId(String jwtToken) {
        Long id = null;

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Elimina el prefijo "Bearer "
        }

        Claims claims = extractAllClaims(jwtToken);

        String subject = claims.getSubject();
        if (subject != null) {
            String[] parts = subject.split("\\|"); // Reemplaza el carácter "|" con el que uses en la construcción del subject
            String userId = parts[1];


            id = Long.valueOf(userId);
        } else {
            System.out.println("No se encontró el campo 'subject' en el token.");
        }
        return id;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
