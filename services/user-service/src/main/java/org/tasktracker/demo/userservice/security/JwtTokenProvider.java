package org.tasktracker.demo.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.domain.model.UserIdentity;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Date;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:42:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
final class JwtTokenProvider implements TokenProvider {
    private final Scheduler scheduler;
    private final KeyProvider keyProvider;
    private final JwtProperties jwtProperties;

    private volatile JwtParser jwtParser;

    @PostConstruct
    public void init() {
        refreshJwtParser();
    }

    @Override
    public Mono<String> generateAccessToken(UserIdentity user) {
        return Mono.fromCallable(() -> {
                    Date now = new Date();
                    Date exp = new Date(now.getTime() + jwtProperties.expiration());

                    return Jwts.builder()
                            .id(UUID.randomUUID().toString())
                            .subject(user.getUsername())
                            .issuer(jwtProperties.issuer())
                            .issuedAt(now)
                            .expiration(exp)
                            .claim("roles", user.roles())
                            .claim("authorities", user.authorities())
                            .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
                            .compact();
                })
                .subscribeOn(scheduler)
                .doOnError(error -> log.error("Token generation failed for user {}", user.username(), error));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Claims claims = jwtParser.parseSignedClaims(token)
                        .getPayload();
                return !claims.getExpiration().before(new Date());
            } catch (JwtException | IllegalArgumentException ex) {
                log.debug("Token validation failed: {}", ex.getMessage());
                return false;
            }
        }).subscribeOn(scheduler);
    }

    @Override
    public Mono<String> extractUsername(String token) {
        return extractClaims(token)
                .map(Claims::getSubject);
    }

    @Override
    public Mono<Claims> extractClaims(String token) {
        return Mono.fromCallable(() -> {
            try {
                return jwtParser.parseSignedClaims(token).getPayload();
            } catch (JwtException ex) {
                throw new BadCredentialsException("Invalid token", ex);
            }
        }).subscribeOn(scheduler);
    }

    private void refreshJwtParser() {
        this.jwtParser = Jwts.parser()
                .verifyWith(keyProvider.getPublicKey())
                .clockSkewSeconds(30)
                .build();
    }
}