package org.tasktracker.demo.userservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
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
@Component
public class DelegatingReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final List<ReactiveAuthenticationManager> authenticationManagers;

    @Autowired
    public DelegatingReactiveAuthenticationManager(JwtAuthenticationManager jwtAuthenticationManager,
                                                   BasicAuthenticationManager basicAuthenticationManager) {
        this.authenticationManagers = List.of(jwtAuthenticationManager, basicAuthenticationManager);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Flux.fromIterable(authenticationManagers)
                .concatMap(manager -> manager.authenticate(authentication)
                        .onErrorResume(error -> {
                            log.debug("Authentication manager {} can not process {}",
                                    manager.getClass().getSimpleName(), error.getMessage());
                            return Mono.empty();
                        }))
                .next()
                .switchIfEmpty(Mono.error(new AuthenticationException("No authentication manager could process the request")));
    }
}