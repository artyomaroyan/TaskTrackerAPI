package org.tasktracker.demo.userservice.application.service;

import jakarta.validation.constraints.NotNull;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.11.25
 * Time: 23:49:55
 */
public interface AuthenticationService {
    Mono<String> login(@NotNull AuthenticationRequest request);
}