package org.tasktracker.demo.user.application.ports.out;

import io.jsonwebtoken.Claims;
import org.tasktracker.demo.user.domain.model.UserIdentity;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:28:29
 */
public interface TokenProvider {
    Mono<String> generateAccessToken(UserIdentity user);
    Mono<Boolean> validateToken(String token);
    Mono<String> extractUsername(String token);
    Mono<Claims> extractClaims(String token);
}