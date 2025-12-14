package org.tasktracker.demo.userservice.security;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.domain.model.UserIdentity;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;
import reactor.core.publisher.Mono;

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
public class JwtTokenProvider implements TokenProvider {
    private final KeyProvider keyProvider;
    private final JwtProperties jwtProperties;

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
                .doOnError(error -> log.error("Token generation failed for user {}", user.username(), error));
    }
}