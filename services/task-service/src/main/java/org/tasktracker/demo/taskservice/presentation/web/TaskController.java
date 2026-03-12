package org.tasktracker.demo.taskservice.presentation.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskResponse;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.application.ports.in.TaskService;
import org.tasktracker.demo.taskservice.domain.model.Status;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.01.26
 * Time: 22:18:58
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/auth/create")
    ResponseEntity<Mono<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        UUID userId = extractUserId(authentication);
        var response = taskService.createTask(request, userId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/auth/update")
    ResponseEntity<Mono<TaskResponse>> updateTask(@Valid @RequestBody TaskUpdateRequest request, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        var response = taskService.updateTask(userId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/status-change")
    ResponseEntity<Mono<TaskResponse>> changeTaskStatus(@RequestBody Status newStatus, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        var response = taskService.changeStatus(userId, newStatus);
        return ResponseEntity.ok(response);
    }

    private UUID extractUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }
}