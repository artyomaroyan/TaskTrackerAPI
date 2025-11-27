package org.tasktracker.demo.userservice.application.ports.out;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.11.25
 * Time: 14:32:12
 */
public interface UserDetailsProvider {
    Mono<UserDetails> findByUsername(@NotNull String username);
    Mono<Boolean> validCredentials(@NotNull String username, @NotNull String password);
}