package org.tasktracker.demo.taskservice.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 26.12.25
 * Time: 22:17:43
 */
@Component
public final class SecurityContextService {

    public static Mono<UUID> currentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .map(token -> token.getToken().getClaimAsString("userId"))
                .map(UUID::fromString);
    }
}