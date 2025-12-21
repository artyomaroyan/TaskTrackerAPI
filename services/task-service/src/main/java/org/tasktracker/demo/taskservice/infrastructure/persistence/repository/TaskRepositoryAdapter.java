package org.tasktracker.demo.taskservice.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.taskservice.domain.model.Task;
import org.tasktracker.demo.taskservice.domain.repository.TaskRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:54:03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private final TaskReactiveRepository reactiveRepository;

    @Override
    public Mono<Task> save(Task task) {
        log.debug("Creating task with ID: {}", task.getId());
        return Mono.just(task)
                ;
    }

    @Override
    public Mono<Task> findTaskById(UUID taskId) {
        return null;
    }

    @Override
    public Mono<Task> findTaskByTitle(String title) {
        return null;
    }

    @Override
    public Flux<Task> findAllTasks(Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Task> findTaskByAssigneeId(UUID assigneeId) {
        return null;
    }
}