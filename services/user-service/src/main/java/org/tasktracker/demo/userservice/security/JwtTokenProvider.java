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
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.UserIdentity;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

                    Set<String> roleName = user.roles().stream()
                            .map(Role::name)
                            .collect(Collectors.toUnmodifiableSet());

                    return Jwts.builder()
                            .id(UUID.randomUUID().toString())
                            .subject(user.getUsername())
                            .issuer(jwtProperties.issuer())
                            .issuedAt(now)
                            .expiration(exp)
                            .claim("roles", roleName)
                            .claim("authorities", user.authorities())
                            .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
                            .compact();
                })
                .subscribeOn(scheduler)
                .doOnSuccess(_ -> log.debug("Token successfully generated."))
                .doOnError(error -> log.error("Token generation failed for user {}", user.username(), error));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return extractClaims(token)
                .map(claims -> !claims.getExpiration().before(new Date()))
                .onErrorReturn(false)
                .doOnError(error -> log.debug("Token validation failed: {}", error.getMessage()));
    }

    @Override
    public Mono<String> extractUsername(String token) {
        return extractClaims(token)
                .map(Claims::getSubject)
                .onErrorResume(JwtException.class,
                        ex -> Mono.error(new BadCredentialsException("Invalid token", ex)));
    }

    @Override
    public Mono<Claims> extractClaims(String token) {
        return Mono.fromCallable(() -> jwtParser.parseSignedClaims(token).getPayload())
                .subscribeOn(scheduler)
                .onErrorMap(JwtException.class,
                        ex -> new BadCredentialsException("Invalid token", ex));

    }

    private void refreshJwtParser() {
        this.jwtParser = Jwts.parser()
                .verifyWith(keyProvider.getPublicKey())
                .clockSkewSeconds(30)
                .build();
    }
}