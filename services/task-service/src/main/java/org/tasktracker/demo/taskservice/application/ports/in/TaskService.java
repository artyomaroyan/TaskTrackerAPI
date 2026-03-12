package org.tasktracker.demo.taskservice.application.ports.in;

import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskResponse;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.domain.model.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:37:15
 */
public interface TaskService {
    Mono<TaskResponse> createTask(TaskRequest request, UUID userId);
    Mono<TaskResponse> updateTask(UUID id, TaskUpdateRequest request);
    Mono<TaskResponse> changeStatus(UUID id, Status status);
    Flux<TaskResponse> listUsersTasks(TaskFilter filter);
    Mono<TaskResponse> findTaskById(UUID id);
    Flux<TaskResponse> findTaskByAssigneeId(UUID assigneeId);
    Mono<TaskResponse> findTaskByTitle(String title);
    Mono<Void> deleteTask(UUID id);
}