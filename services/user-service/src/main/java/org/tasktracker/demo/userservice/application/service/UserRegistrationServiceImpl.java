package org.tasktracker.demo.userservice.application.service;

import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.dto.UserResponse;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:52:49
 */
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    @Override
    public Mono<UserResponse> register(UserRequest request) {
        return null;
    }
}