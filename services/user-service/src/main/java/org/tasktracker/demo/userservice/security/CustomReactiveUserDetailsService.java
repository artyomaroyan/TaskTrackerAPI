package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import reactor.core.publisher.Mono;

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
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("Looking for user with username: {}", username);

        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("No user found with username: {}", username);
                    return Mono.error(new UsernameNotFoundException("User not found: " + username));
                }))
                .map(user -> {
                    log.debug("Found user: {}", user.username());

                    return new UserIdentity(
                            user.username(),
                            user.password(),
                            user.getAuthorities(),
                            user.active()
                    );
                });
    }

    public Mono<Boolean> validCredentials(String username, String rawPassword) {
        return findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .defaultIfEmpty(false);
    }
}