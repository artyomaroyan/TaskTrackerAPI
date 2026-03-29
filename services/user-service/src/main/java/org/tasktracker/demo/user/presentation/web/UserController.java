package org.tasktracker.demo.user.presentation.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasktracker.demo.user.application.dto.AuthRequest;
import org.tasktracker.demo.user.application.dto.UserRequest;
import org.tasktracker.demo.user.application.dto.UserResponse;
import org.tasktracker.demo.user.application.ports.in.AuthenticationService;
import org.tasktracker.demo.user.application.ports.in.UserRegistrationService;
import org.tasktracker.demo.user.application.ports.in.UserService;
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
    private final AuthenticationService authenticationService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    ResponseEntity<Mono<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        var response = userRegistrationService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/id/{userId}")
    ResponseEntity<Mono<UserResponse>> findUserById(@PathVariable UUID userId) {
        var response = userService.findUserById(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/username/{username}")
    ResponseEntity<Mono<UserResponse>> findUserByUsername(@PathVariable String username) {
        var response = userService.findUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    ResponseEntity<Mono<Void>> deleteUserById(@PathVariable UUID userId) {
        var response = userService.deleteUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    ResponseEntity<Mono<String>> login(@Valid @RequestBody AuthRequest request) {
        var response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}