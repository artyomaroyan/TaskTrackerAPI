package org.tasktracker.demo.userservice.domain.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

import java.util.Set;

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
                .switchIfEmpty(Mono.error(new UserNotFoundException(
                        String.format("User with username %s not found!", username))))
                .map(this::toUserIdentity)
                .cast(UserDetails.class)
                .doOnError(error -> log.error("Error loading user: {}", username, error));
    }

    private UserIdentity toUserIdentity(User user) {
        Set<Role> roles = Set.of(user.role());
        Set<String> authorities = user.getAuthorities();

        return new UserIdentity(
                user.id(),
                user.username(),
                user.password(),
                roles,
                authorities,
                user.active()
        );
    }
}