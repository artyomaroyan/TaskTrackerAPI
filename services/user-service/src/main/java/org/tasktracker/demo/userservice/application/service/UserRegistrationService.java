package org.tasktracker.demo.userservice.application.service;

import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.dto.UserResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:47:31
 */
public interface UserRegistrationService {
    Mono<UserResponse> register(UserRequest request);
}