package org.tasktracker.demo.userservice.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 19:37:10
 */
@Component
@RequiredArgsConstructor
public class JwtFilter implements WebFilter {
    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    // keep excluded path's in separate Set in case if in future they become more.
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/v1/auth"
    );

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull final ServerWebExchange exchange, @NonNull final WebFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();
        if (EXCLUDED_PATHS.contains(path)) return chain.filter(exchange);

        return extractTokenFromRequest(exchange)
                .flatMap(token -> {
                    String username = jwtService.extractUsername(token);
                    if (username != null) {
                        return userDetailsService.findByUsername(username)
                                .filter(userDetails ->
                                        jwtService.isTokenValid(token, userDetails.getUsername()))

                                .flatMap(userDetails -> {
                                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities()
                                    );

                                    return chain.filter(exchange)
                                            .contextWrite(ReactiveSecurityContextHolder
                                                    .withAuthentication(authenticationToken));
                                });
                    }
                    return chain.filter(exchange);
                });
    }

    private Mono<String> extractTokenFromRequest(final ServerWebExchange exchange) {
        final List<String> authHeaders = exchange.getRequest()
                .getHeaders()
                .getOrEmpty(HttpHeaders.AUTHORIZATION);
        return Mono.justOrEmpty(authHeaders.stream()
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .findFirst());
    }
}