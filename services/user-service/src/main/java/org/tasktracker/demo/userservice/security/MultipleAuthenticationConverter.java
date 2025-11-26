package org.tasktracker.demo.userservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 17:51:53
 */
@Slf4j
public class MultipleAuthenticationConverter implements ServerAuthenticationConverter {
    private static final String BASIC_PREFIX = "Basic ";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractAuthHeader(exchange)
                .flatMap(this::createAuthenticationToken)
                .switchIfEmpty(Mono.empty());
    }

    private Mono<String> extractAuthHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(AUTHORIZATION));
    }

    private Mono<Authentication> createAuthenticationToken(String authHeader) {
        if (authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            return Mono.just(new BearerToken(token));
        } else if (authHeader.startsWith(BASIC_PREFIX)) {
            return extractBasicAuth(authHeader);
        }
        return Mono.empty();
    }

    private Mono<Authentication> extractBasicAuth(String authHeader) {
        return Mono.fromCallable(() -> {
                    String base64Credentials = authHeader.substring(BASIC_PREFIX.length()).trim();
                    byte[] decoded = Base64.getDecoder().decode(base64Credentials);
                    String credentials = new String(decoded, StandardCharsets.UTF_8);

                    final String[] values = credentials.split(":", 2);

                    if (values.length != 2) throw new AuthenticationException("Invalid basic authentication format");

                    String username = values[0];
                    String password = values[1];

                    log.debug("Basic Auth attempt for user: {}", username);

                    return new UsernamePasswordAuthenticationToken(username, password);
                }).cast(Authentication.class)
                .onErrorResume(error -> {
                    log.warn("Basic authentication extraction failed: {}", error.getMessage());
                    return Mono.empty();
                });
    }
}