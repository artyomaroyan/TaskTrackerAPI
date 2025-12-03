package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.12.25
 * Time: 01:36:48
 */
@Component
@RequiredArgsConstructor
public class ReactiveSecurityService {
    private final UserRepository userRepository;

    public Mono<Boolean> isSelfOrAdmin(UUID userId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                    if (isAdmin) {
                        return Mono.just(true);
                    }

                    String currentUsername = authentication.getName();
                    return userRepository.findByUsername(currentUsername)
                            .map(user -> user.getId().equals(userId))
                            .defaultIfEmpty(false);
                })
                .defaultIfEmpty(false);
    }
}