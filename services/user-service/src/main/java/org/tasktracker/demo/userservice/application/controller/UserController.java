package org.tasktracker.demo.userservice.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.service.UserRegistrationService;
import org.tasktracker.demo.userservice.application.service.UserService;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:54:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    ResponseEntity<Mono<User>> register(@Valid @RequestBody UserRequest request) {
        var response = userRegistrationService.register(request);
        return ResponseEntity.ok(response);
    }
}