package org.tasktracker.demo.task.application.port.in;

import org.tasktracker.demo.task.application.dto.TaskFilter;
import org.tasktracker.demo.task.application.dto.TaskRequest;
import org.tasktracker.demo.task.application.dto.TaskResponse;
import org.tasktracker.demo.task.domain.model.Status;
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
    Mono<TaskResponse> updateTask(UUID taskId, UUID userId, TaskRequest request);
    Mono<TaskResponse> changeStatus(UUID taskId, UUID userId, Status status);
    Flux<TaskResponse> findAllTasks();
    Flux<TaskResponse> findAllTasksByStatus(UUID userId, Status status);
    Mono<TaskResponse> findTaskById(UUID userId, UUID taskId);
    Flux<TaskResponse> findTaskByAssigneeId(UUID assigneeId);
    Flux<TaskResponse> findTaskByTitle(String title);
    Flux<TaskResponse> findTaskByFilter(TaskFilter filter);
    Mono<Void> deleteTask(UUID taskId);
}