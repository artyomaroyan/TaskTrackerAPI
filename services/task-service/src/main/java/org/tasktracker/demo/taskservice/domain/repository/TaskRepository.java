package org.tasktracker.demo.taskservice.domain.repository;

import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.domain.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:37:46
 */
public interface TaskRepository {
    Mono<Task> save(Task task);
    Flux<Task> findAllTasks(TaskFilter filter);
    Mono<Task> findTaskById(UUID taskId);
    Mono<Void> deleteTask(UUID id);
}