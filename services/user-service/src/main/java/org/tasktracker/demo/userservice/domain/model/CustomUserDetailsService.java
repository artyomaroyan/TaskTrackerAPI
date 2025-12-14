package org.tasktracker.demo.userservice.domain.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import org.tasktracker.demo.userservice.security.UserIdentity;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 19:24:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found!")))
                .map(user -> new UserIdentity(
                        user.username(),
                        null,
                        user.role(),
                        user.getAuthorities(),
                        user.active()
                ));
    }
}