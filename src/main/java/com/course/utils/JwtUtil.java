package com.course.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.course.entity.TableEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private String secretKey;

    private Long expiration;

    public String generateTableToken(TableEntity tableEntity) {

        return Jwts.builder()
                .setSubject("tableToken")
                .claim("tableId", tableEntity.getId())
                .claim("openedAt", tableEntity.getOpenedAt().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateTableToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token).getBody();
    }
}
