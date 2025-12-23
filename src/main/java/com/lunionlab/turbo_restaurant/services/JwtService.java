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

@Service
public class JwtService {

    @Value("${jwt.time-exp}")
    private Integer TIME_EXP;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, String audience) {
        Date dateExp = Utility.dateFromInteger(TIME_EXP * 60, ChronoUnit.MINUTES);

        return Jwts.builder().setAudience(audience).setExpiration(dateExp).setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Map<String, String> getInfoFromToken(String token) {
        Map<String, String> infos = new HashMap<>();
        infos.put("audience", "");
        infos.put("identifier", "");
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            infos.put("audience", claims.getAudience());
            infos.put("identifier", claims.getSubject());
            return infos;
        } catch (Exception e) {
            return infos;
        }
    }
}
