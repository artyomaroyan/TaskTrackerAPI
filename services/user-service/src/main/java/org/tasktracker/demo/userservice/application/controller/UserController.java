package org.tasktracker.demo.userservice.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.service.UserRegistrationService;
import org.tasktracker.demo.userservice.application.service.UserService;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

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

    @GetMapping("/get/{userId}")
    ResponseEntity<Mono<User>> findUserById(@PathVariable UUID userId) {
        var response = userService.findUserById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{username}")
    ResponseEntity<Mono<User>> findUserByUsername(@PathVariable String username) {
        var response = userService.findUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    ResponseEntity<Mono<Void>> deleteUserById(@PathVariable UUID userId) {
        var response = userService.deleteUserById(userId);
        return ResponseEntity.ok(response);
    }
}