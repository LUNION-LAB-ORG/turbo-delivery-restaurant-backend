package com.lunionlab.turbo_restaurant.services;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.nio.charset.StandardCharsets;   // pour StandardCharsets.UTF_8
import java.security.Key;                   // pour Key
import io.jsonwebtoken.security.Keys;      // pour Keys


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lunionlab.turbo_restaurant.utilities.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.time-exp}")
    private Integer TIME_EXP;

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public String generateToken(String username, String audience) {
        Date dateExp = Utility.dateFromInteger(TIME_EXP * 60, ChronoUnit.MINUTES);
        return Jwts.builder()
            .setAudience(audience)
            .setExpiration(dateExp)
            .setSubject(username)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256) // <-- utiliser Key
            .compact();
    }
    
    public Map<String, String> getInfoFromToken(String token) {
        Map<String, String> infos = new HashMap<>();
        infos.put("audience", "");
        infos.put("identifier", "");
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            infos.put("audience", claims.getAudience());
            infos.put("identifier", claims.getSubject());
            log.info("Token parsed successfully: audience={}, identifier={}", claims.getAudience(), claims.getSubject());
        } catch (Exception e) {
            log.error("Failed to parse token", e);
        }
        return infos;
    }
}
