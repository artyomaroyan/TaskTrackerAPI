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
                            .anyMatch(auth -> auth.
                                    getAuthority().equals(SecurityConstants.ROLE_ADMIN));

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

    public Mono<Boolean> hasAuthority(String authority) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals(authority)))
                .defaultIfEmpty(false);
    }

    public Mono<UUID> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> userRepository.findByUsername(authentication.getName())
                        .map(User::getId));
    }

    public Mono<Boolean> isResourceOwner(UUID resourceId, String resourceType) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth
                                    .getAuthority().equals(SecurityConstants.ROLE_ADMIN));

                    if (isAdmin) {
                        return Mono.just(true);
                    }

                    return checkResourceOwnership(resourceId, resourceType, authentication.getName());
                })
                .defaultIfEmpty(false);
    }

    private Mono<Boolean> checkResourceOwnership(UUID resourceId, String resourceType, String username) {
        return switch (resourceType) {
            case "USER" -> userRepository.findById(resourceId)
                    .map(user -> user.getUsername().equals(username))
                    .defaultIfEmpty(false);
            case "ADMIN" -> Mono.just(true);
            default -> Mono.just(false);
        };
    }
}