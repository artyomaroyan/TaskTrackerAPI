package org.tasktracker.demo.userservice.application.ports.in;

import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.dto.UserResponse;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:47:31
 */
public interface UserRegistrationService {
    Mono<UserResponse> register(@NotNull UserRequest request);
}