package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 19:12:44
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicAuthenticationManager implements ReactiveAuthenticationManager {
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> auth instanceof UsernamePasswordAuthenticationToken)
                .cast(UsernamePasswordAuthenticationToken.class)
                .switchIfEmpty(Mono.empty())
                .flatMap(this::authenticateBasic)
                .doOnError(error -> log.warn("Basic authentication failed: {}", error.getMessage()));
    }

    private Mono<Authentication> authenticateBasic(UsernamePasswordAuthenticationToken authenticationToken) {
        String username = authenticationToken.getName();
        String password = authenticationToken.getCredentials().toString();

        if (username == null || password == null) {
            return Mono.error(new BadCredentialsException("Username or password can not be null!"));
        }

        return userDetailsService.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found!")))
                .filter(userDetails -> passwordEncoder.matches(password, userDetails.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials!")))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ));
    }
}