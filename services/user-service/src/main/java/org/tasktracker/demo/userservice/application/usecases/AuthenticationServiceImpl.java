package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import org.tasktracker.demo.userservice.application.ports.in.AuthenticationService;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.domain.model.UserIdentity;
import reactor.core.publisher.Mono;

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
    public Mono<String> login(AuthenticationRequest request) {
        return userDetailsService.findByUsername(request.username())
                .cast(UserIdentity.class)
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials!")))
                .map(UserIdentity::withoutPassword)
                .flatMap(tokenProvider::generateAccessToken)
                .doOnSuccess(_ -> log.debug("Generated token for user: {}", request.username()))
                .doOnError(error -> {
                    if (!(error instanceof BadCredentialsException)) {
                        log.error("Login failed for user: {}", request.username(), error);
                    }
                });
    }

//    @Override
//    @PreAuthorize("permitAll()")
//    public Mono<String> login(AuthenticationRequest request) {
//        return userDetailsService.findByUsername(request.username())
//                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found!")))
//                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
//                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid credentials")))
//                .map(user -> new UserIdentity(
//                        user.getUsername(),
//                        null,
//                        extractRoles(user),
//                        extractAuthorities(user),
//                        user.isEnabled()
//                ))
//                .flatMap(tokenProvider::generateAccessToken);
//    }
//
//    private Set<Role> extractRoles(final UserDetails userDetails) {
//        return userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .filter(r -> r.startsWith("ROLE_"))
//                .map(r -> r.substring(5))
//                .collect(Collectors.toUnmodifiableSet());
//    }
//
//    private Set<String> extractAuthorities (final UserDetails userDetails) {
//        return userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toUnmodifiableSet());
//    }
}