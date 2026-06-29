package com.modsProject.modsProject.server.security.services;

import com.modsProject.modsProject.server.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    private final SecretKey secretKey;
    private final long expiration;

    public JwtService() {
        this.secretKey = Keys.hmacShaKeyFor("0lNWcBE4VkyKvCDpnGT10FVxOIKbZwz48r3QaRjYDhn".getBytes(StandardCharsets.UTF_8));
        this.expiration = 1000 * 60 * 16; //15 минут
    }

    public String generateToken(Long creator_id) {
        return Jwts.builder()
                .subject(creator_id.toString())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(secretKey)
                .compact();
    }

    public JwtTokenDto parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtTokenDto(
                Long.parseLong(claims.getSubject()),
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant()
        );
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.debug("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException e) {
            LOGGER.debug("Invalid token: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.debug("Unexpected token error", e);
        }

        return false;
    }
}