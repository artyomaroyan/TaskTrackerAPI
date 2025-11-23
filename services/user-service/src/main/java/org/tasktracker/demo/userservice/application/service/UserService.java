package org.tasktracker.demo.userservice.application.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:45:27
 */
public interface UserService {
    Mono<User> findUserById(@NotNull UUID id);
    Mono<User> findUserByUsername(@NotBlank String username);
    Mono<Void> deleteUserById(@NotNull UUID id);
}