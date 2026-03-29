package org.tasktracker.demo.task.application.port.out;

import org.tasktracker.demo.task.application.dto.TaskFilter;
import org.tasktracker.demo.task.domain.model.Task;
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
    Flux<Task> findAllTasks();
    Flux<Task> findTaskByTitle(String title);
    Flux<Task> findTasksByFilter(TaskFilter filter);
    Mono<Task> findTaskById(UUID taskId);
    Flux<Task> findTaskByAssigneeId(UUID assigneeId);
    Mono<Void> deleteTask(UUID id);
}