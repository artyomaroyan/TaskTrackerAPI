package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import org.tasktracker.demo.userservice.application.ports.in.AuthenticationService;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import org.tasktracker.demo.userservice.security.UserIdentity;
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
    @PreAuthorize("permitAll()")
    public Mono<String> login(AuthenticationRequest request) {
        return userDetailsService.findByUsername(request.username())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")))
                .flatMap(userDetails -> {
                    boolean match = passwordEncoder.matches(request.password(), userDetails.getPassword());
                    if (!match) {
                        log.debug("invalid password");
                        return Mono.error(new BadCredentialsException("Invalid credentials"));
                    }
                    return Mono.just(userDetails);
                })
                .flatMap(userDetails -> tokenProvider.generateAccessToken((UserIdentity) userDetails))
                .doOnSuccess(_ -> log.info("Successfully login"))
                .doOnError(error -> log.error("login filed ", error));
    }
}