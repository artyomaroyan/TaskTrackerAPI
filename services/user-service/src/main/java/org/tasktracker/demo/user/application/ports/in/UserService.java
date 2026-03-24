package org.tasktracker.demo.user.application.ports.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.user.application.dto.UserResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:45:27
 */
public interface UserService {
    Mono<UserResponse> findUserById(@NotNull UUID id);
    Mono<UserResponse> findUserByUsername(@NotBlank String username);
    Mono<Void> deleteUserById(@NotNull UUID id);
}