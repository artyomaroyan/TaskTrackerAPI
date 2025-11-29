package org.tasktracker.demo.userservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 19:12:44
 */
@Slf4j
public record BasicAuthenticationManager(
        PasswordEncoder passwordEncoder,
        ReactiveUserDetailsService userDetailsService) implements ReactiveAuthenticationManager {

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
                .map(userDetails -> {
                    UserIdentity userIdentity = new UserIdentity(
                            userDetails.getUsername(),
                            extractAuthorities(userDetails),
                            userDetails.isEnabled()
                    );

                    return new UsernamePasswordAuthenticationToken(
                            userIdentity,
                            null,
                            userDetails.getAuthorities()
                    );
                });
    }

    private Set<String> extractAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}