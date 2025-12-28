package org.tasktracker.demo.taskservice.application.ports.in;

import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.domain.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:37:15
 */
public interface TaskService {
    Mono<Task> createTask(TaskRequest request);
    Mono<Task> updateTask(UUID id, TaskUpdateRequest request);
    Flux<Task> listUsersTasks(TaskFilter filter);
    Mono<Task> findTaskById(UUID id);
    Mono<Task> changeStatus(UUID id, String status);
    Mono<Void> deleteTask(UUID id);
}