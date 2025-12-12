package org.tasktracker.demo.userservice.application.ports.out;

import org.springframework.security.core.Authentication;
import org.tasktracker.demo.userservice.security.UserIdentity;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:28:29
 */
public interface TokenProvider {
    Mono<String> generateAccessToken(UserIdentity user);
    Mono<Boolean> validateToken(String token);
}