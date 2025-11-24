package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 18:01:20
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String token = auth.getCredentials();
                    String username = jwtService.extractUsername(token);

                    return userDetailsService.findByUsername(username)
                            .switchIfEmpty(Mono.error(new UserNotFoundException("User not found!")))
                            .flatMap(userDetails -> {
                                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                                    return Mono.justOrEmpty(
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails, userDetails.getAuthorities()
                                            ));
                                } else {
                                    return Mono.error(new IllegalArgumentException("Invalid token!"));
                                }
                            });
                });
    }
}