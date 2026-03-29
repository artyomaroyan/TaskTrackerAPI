package org.tasktracker.demo.user.application.ports.in;

import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.user.application.dto.AuthRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.11.25
 * Time: 23:49:55
 */
public interface AuthenticationService {
    Mono<String> login(@NotNull AuthRequest request);
}