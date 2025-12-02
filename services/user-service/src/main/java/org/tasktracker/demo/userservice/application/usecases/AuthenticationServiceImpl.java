package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import org.tasktracker.demo.userservice.application.ports.in.AuthenticationService;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import org.tasktracker.demo.userservice.security.UserIdentity;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 23.11.25
 * Time: 23:51:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    @PreAuthorize("permitAll()")
    public Mono<String> login(AuthenticationRequest request) {
        log.debug("Processing login for user {}", request.username());
        return userDetailsService.findByUsername(request.username())
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("User not found: {}", request.username());
                    return Mono.error(new UserNotFoundException("User not found: " + request.username()));
                }))
                .filter(userDetails -> {
                    boolean matches = passwordEncoder.matches(request.password(), userDetails.getPassword());
                    if (!matches) {
                        log.debug("Password mismatch for user: {}", request.username());
                    }
                     return matches;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Invalid credentials for user: {}", request.username());
                    return Mono.error(new BadCredentialsException("Invalid credentials"));
                }))
                .map(userDetails -> {
                    Set<String> authorities = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());

                    return new UserIdentity(
                            request.username(),
                            authorities,
                            userDetails.isEnabled()
                    );
                })
                .flatMap(tokenProvider::generateAccessToken)
                .doOnSuccess(_ -> log.debug("Successfully longed in: {}", request.username()))
                .doOnError(error -> log.error("Login failed for user: {}: {}", request.username(), error.getMessage()));
    }
}