package org.tasktracker.demo.userservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.AuthenticationException;
import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 25.11.25
 * Time: 18:05:54
 */
@Slf4j
public class DelegatingReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final List<ReactiveAuthenticationManager> authenticationManagers;

    public DelegatingReactiveAuthenticationManager(BasicAuthenticationManager basicAuthenticationManager,
                                                   JwtAuthenticationManager jwtAuthenticationManager) {
        this.authenticationManagers = List.of(basicAuthenticationManager, jwtAuthenticationManager);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.debug("DelegatingReactiveAuthenticationManager: Processing authentication type: {}",
                authentication != null ? authentication.getClass().getSimpleName() : "Authentication not found");

        if (authentication == null) {
            return Mono.error(new AuthenticationException("Authentication object is null"));
        }

        log.debug("DelegatingReactiveAuthenticationManager: Authentication details - Name: {}, Authenticated: {}",
                authentication.getName(), authentication.isAuthenticated());

        return Flux.fromIterable(authenticationManagers)
                .doOnNext(manager -> log.debug("Trying authentication manager: {}",
                        manager.getClass().getSimpleName()))
                .concatMap(manager -> manager.authenticate(authentication)
                        .doOnNext(_ -> log.debug("Manager {} successfully authenticated",
                                manager.getClass().getSimpleName()))
                        .onErrorResume(error -> {
                            log.debug("Authentication manager {} can not process: {}",
                                    manager.getClass().getSimpleName(), error.getMessage());

                            return Mono.empty();
                        }))
                .next()
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No authentication manager could process the request. Authentication type: {}",
                            authentication.getClass().getSimpleName());
                    return Mono.error(new AuthenticationException("No authentication manager could process the request:"));
                }));
    }
}