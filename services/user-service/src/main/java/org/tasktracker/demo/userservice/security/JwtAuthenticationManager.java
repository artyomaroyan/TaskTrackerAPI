package org.tasktracker.demo.userservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.tasktracker.demo.userservice.exception.TokenValidationException;
import org.tasktracker.demo.userservice.security.interfaces.TokenProvider;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 18:01:20
 */
@Slf4j
public record JwtAuthenticationManager(TokenProvider tokenProvider) implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> auth instanceof BearerToken)
                .cast(BearerToken.class)
                .switchIfEmpty(Mono.empty())
                .flatMap(this::authenticateBearerToken)
                .doOnError(error -> log.warn("Jwt authentication failed: {}", error.getMessage()));
    }

    private Mono<Authentication> authenticateBearerToken(BearerToken bearerToken) {
        String token = bearerToken.getToken();
        return tokenProvider.validateToken(token)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new TokenValidationException("Invalid Token!")))
                .then(tokenProvider.extractUserIdentity(token))
                .map(userIdentity -> new UsernamePasswordAuthenticationToken(
                        userIdentity,
                        token,
                        userIdentity.getAuthorities()
                ));
    }
}