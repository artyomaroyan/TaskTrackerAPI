package org.tasktracker.demo.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:42:33
 */
@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {
    private final JwtParser jwtParser;
    private final KeyProvider keyProvider;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(KeyProvider keyProvider, JwtProperties jwtProperties) {
        this.keyProvider = keyProvider;
        this.jwtProperties = jwtProperties;
        this.jwtParser = Jwts.parser()
                .verifyWith(keyProvider.getPublicKey())
                .build();
    }

    @Override
    public Mono<String> generateAccessToken(UserIdentity user) {
        return Mono.fromCallable(() -> {
            Date now = new Date();
            Date exp = new Date(now.getTime() + jwtProperties.expiration());

            Map<String, Object> claims = Map.of(
                    "sub", user.username(),
                    "iat", now,
                    "exp", exp,
                    "iss", jwtProperties.issuer(),
                    "role", user.roles(),
                    "auth", user.authorities()
            );

            return Jwts.builder()
                    .id(UUID.randomUUID().toString())
                    .claims(claims)
                    .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
                    .compact();
        }).doOnError(error -> log.error("Token generation failed for user {}", user.username(), error));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = jwtParser.parseSignedClaims(token)
                    .getPayload();

            return !claims.getExpiration().before(new Date());
        });
    }
}