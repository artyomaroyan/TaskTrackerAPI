package org.tasktracker.demo.user.application.ports.in;

import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.user.application.dto.UserRequest;
import org.tasktracker.demo.user.application.dto.UserResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:47:31
 */
public interface UserRegistrationService {
    Mono<UserResponse> register(@NotNull UserRequest request);
}