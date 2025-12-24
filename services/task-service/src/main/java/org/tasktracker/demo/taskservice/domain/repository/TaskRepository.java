package org.tasktracker.demo.taskservice.domain.repository;

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
    Mono<Task> findTaskById(UUID taskId);
    Mono<Task> findTaskByTitle(String title);
    Flux<Task> findAllTasks();
    Mono<Task> findTaskByAssigneeId(UUID assigneeId);
}