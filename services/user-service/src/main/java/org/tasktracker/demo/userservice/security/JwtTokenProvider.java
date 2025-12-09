package org.tasktracker.demo.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.exception.TokenValidationException;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.infrastructure.configuration.JwtProperties;
import reactor.core.publisher.Mono;

import java.util.*;

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
                    "authorities", user.authorities()
            );

            return Jwts.builder()
                    .id(UUID.randomUUID().toString())
                    .claims(claims)
                    .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
                    .compact();

            // I like this type of token creating,
            // but I think creating separate claims and set them is more effective and fast version.
//            return Jwts.builder()
//                    .id(UUID.randomUUID().toString())
//                    .subject(user.username())
//                    .issuedAt(now)
//                    .expiration(exp)
//                    .issuer(jwtProperties.issuer())
//                    .claim("authorities", user.authorities())
//                    .claim("test", "this is testing claim to show that you can add claims as you want.")
//                    .signWith(keyProvider.getPrivateKey(), Jwts.SIG.RS256)
//                    .compact();
        }).doOnError(error -> log.error("Token generation failed for user {}", user.username(), error));
    }

    @Override
    public Mono<UserIdentity> extractUserIdentity(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = jwtParser.parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            @SuppressWarnings("unchecked")
            List<String> authorityStrings = claims.get("authorities", List.class);

            if (username == null || username.isBlank())
                throw new TokenValidationException("Token subject (username) is missing!");

            Set<String> authorities = new HashSet<>(authorityStrings);

            return new UserIdentity(
                    username,
                    null,
                    authorities,
                    true
            );
        }).onErrorMap(JwtException.class, ex ->
                new TokenValidationException("Invalid token: " + ex.getMessage()));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return extractUserIdentity(token)
                .map(_ -> true)
                .onErrorReturn(false);
    }

    @Override
    public Mono<Boolean> isTokenExpired(String token) {
        return extractUserIdentity(token)
                .map(_ -> false)// If we can extract, it's not expired
                .onErrorResume(TokenValidationException.class, _ -> Mono.just(true));
    }
}