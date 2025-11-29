package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 19:24:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Looking for user with username: {}", username);
        return userRepository.findByUsername(username)
                .doOnNext(user -> log.debug("Found user: {} with username: {}", user.getUsername(), username))
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("No user found with username: {}", username);
                    return Mono.error(new UsernameNotFoundException("User not found: " + username));
                }))
                .map(user -> {
                    log.debug("Building UserDetails for: {}", user.getUsername());
                    return User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getAuthorities().stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toSet()))
                            .build();
                });
    }
}