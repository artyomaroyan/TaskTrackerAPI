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
import org.tasktracker.demo.taskservice.domain.exception.TaskValidationException;
import org.tasktracker.demo.taskservice.domain.model.Status;
import reactor.core.publisher.Flux;
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
    Mono<ResponseEntity<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        return taskService.createTask(request, userId)
                .map(ResponseEntity::ok)
                .onErrorResume(TaskValidationException.class,
                        _ -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/auth/update")
    Mono<ResponseEntity<TaskResponse>> updateTask(@Valid @RequestBody TaskUpdateRequest request, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        return taskService.updateTask(userId, request)
                .map(ResponseEntity::ok)
                .onErrorResume(TaskValidationException.class,
                        _ -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PatchMapping("/auth/status-change")
    Mono<ResponseEntity<TaskResponse>> changeTaskStatus(@RequestBody Status newStatus, Authentication authentication) {
        UUID userId = extractUserId(authentication);
       return taskService.changeStatus(userId, newStatus)
               .map(ResponseEntity::ok)
               .onErrorResume(TaskValidationException.class,
                       _ -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/auth/get/all-tasks")
    Flux<ResponseEntity<TaskResponse>> getAllTasks() {
            return taskService.findAllTasks()
                    .map(ResponseEntity::ok)
                    .onErrorResume(TaskValidationException.class,
                            _ -> Flux.just(ResponseEntity.badRequest().build()));
    }

    private UUID extractUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        log.info("printing user roles and authorities: {}, {}, {}", jwt.getClaimAsString("roles"), jwt.getClaimAsString("authorities"), authentication.getAuthorities());
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }
}