package org.tasktracker.demo.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 19:37:44
 */
@Service
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${spring.security.jwt.issuer}")
    private String jwtIssuer;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey();
        this.publicKey = KeyUtils.loadPublicKey();
    }

    public String generateAccessToken(final String username) {
        final Map<String, Object> claims = Map.of(
                "claim1", "claim1",
                "claim2", "claim2",
                "claim3", "claim3",
                "claim4", "claim4"
        );
        return generateToken(username, claims);
    }

    public boolean isTokenValid(final String token, final String expectedUsername) {
        final String username = extractUsername(token);
        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    protected String extractUsername(final String token) {
        return Objects.requireNonNull(extractClaims(token)).getSubject();
    }

    private String generateToken(final String subject, final Map<String, Object> claims) {
        final Date now = new Date();
        final Date exp = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .issuedAt(now)
                .expiration(exp)
                .issuer(jwtIssuer)
                .claims(claims)
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    private boolean isTokenExpired(final String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (final JwtException ex) {
            throw new RuntimeException("Invalid token ", ex);
        }
    }
}