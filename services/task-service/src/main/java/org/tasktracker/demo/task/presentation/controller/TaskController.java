package org.tasktracker.demo.task.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.tasktracker.demo.task.application.dto.TaskFilter;
import org.tasktracker.demo.task.application.dto.TaskRequest;
import org.tasktracker.demo.task.application.dto.TaskResponse;
import org.tasktracker.demo.task.application.port.in.TaskService;
import org.tasktracker.demo.task.domain.model.Status;
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
                .map(ResponseEntity::ok);
    }

    @PutMapping("/auth/update")
    Mono<ResponseEntity<TaskResponse>> updateTask(@Valid @RequestBody TaskRequest request, UUID taskId, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        return taskService.updateTask(taskId, userId, request)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/auth/status-change")
    Mono<ResponseEntity<TaskResponse>> changeTaskStatus(@RequestBody Status newStatus, UUID taskId, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        return taskService.changeStatus(taskId, userId, newStatus)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/auth/get/all-tasks")
    Flux<TaskResponse> getAllTasks() {
        return taskService.findAllTasks();
    }

    @GetMapping("/auth/get-by-status")
    Flux<TaskResponse> getAllTasksByStatus(@RequestParam Status status, Authentication authentication) {
        var userId = extractUserId(authentication);
        return taskService.findAllTasksByStatus(userId, status);
    }

    @GetMapping("/auth/get-by-id")
    Mono<TaskResponse> getTaskById(@RequestParam UUID taskId, Authentication authentication) {
        var userId = extractUserId(authentication);
        return taskService.findTaskById(userId, taskId);
    }

    @GetMapping("/auth/get-by-assignee-id")
    Flux<TaskResponse> getTasksByAssigneeId(Authentication authentication) {
        var assigneeId = extractUserId(authentication);
        return taskService.findTaskByAssigneeId(assigneeId);
    }

    @GetMapping("/auth/get-by-title")
    Flux<TaskResponse> getTaskByTitle(@RequestParam String title) {
        return taskService.findTaskByTitle(title);
    }

    @GetMapping("/auth/get-by-filter")
    Flux<TaskResponse> getTasksByFilter(@RequestParam TaskFilter filter) {
        return taskService.findTaskByFilter(filter);
    }

    @DeleteMapping("/auth/delete-by-id")
    Mono<Void> deleteTaskById(@RequestBody UUID taskId) {
        return taskService.deleteTask(taskId)
                .then();
    }

    private UUID extractUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        log.info("printing user roles and authorities: {}, {}, {}", jwt.getClaimAsString("roles"), jwt.getClaimAsString("authorities"), authentication.getAuthorities());
        return UUID.fromString(jwt.getClaimAsString("userId"));
    }
}