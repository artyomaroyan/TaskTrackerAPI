package org.tasktracker.demo.taskservice.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.taskservice.application.mapper.TaskMapper;
import org.tasktracker.demo.taskservice.domain.exception.DataReadingException;
import org.tasktracker.demo.taskservice.domain.exception.DataSavingException;
import org.tasktracker.demo.taskservice.domain.model.Task;
import org.tasktracker.demo.taskservice.domain.repository.TaskRepository;
import org.tasktracker.demo.taskservice.infrastructure.persistence.entity.TaskEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
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
    private final TaskMapper taskMapper;
    private final TaskReactiveRepository reactiveRepository;

    @Override
    public Mono<Task> save(Task task) {
        log.debug("Creating task with ID: {}", task.id());
        return Mono.just(task)
                .flatMap(taskMapper::toEntity)
                .flatMap(reactiveRepository::save)
                .flatMap(taskMapper::toDomain)
                .doOnSuccess(saved -> log.debug("Task {} was successfully saved.", saved.id()))
                .onErrorMap(DataAccessException.class, ex ->
                        new DataSavingException("Failed to create task with id: " + task.id(), ex));
    }

    @Override
    public Mono<Task> findTaskById(UUID taskId) {
        log.debug("Finding task with ID {}", taskId);
        return reactiveRepository.findById(taskId)
                .flatMap(taskMapper::toDomain)
                .doOnSuccess(_ -> log.debug("Found task with ID: {}", taskId))
                .onErrorMap(DataAccessException.class, ex ->
                        new DataReadingException("Failed to find task with ID: " + taskId, ex));
    }

    @Override
    public Mono<Task> findTaskByTitle(String title) {
        return reactiveRepository.findByTitle(title)
                .flatMap(taskMapper::toDomain)
                .doOnSuccess(_ -> log.debug("Found task by title: {}", title))
                .onErrorMap(DataAccessException.class, ex ->
                        new DataReadingException("Failed to found task with title: " + title, ex));
    }

    @Override
    public Flux<Task> findAllTasks() {
        return reactiveRepository.findAll()
                .sort(Comparator.comparing(TaskEntity::getCreatedAt))
                .flatMap(taskMapper::toDomain)
                .doOnComplete(() -> log.debug("Tasks fetch successfully."))
                .onErrorMap(DataAccessException.class, ex ->
                         new DataReadingException("Unable to load all tasks", ex.getCause()));
    }

    @Override
    public Mono<Task> findTaskByAssigneeId(UUID assigneeId) {
        return reactiveRepository.findByAssigneeId(assigneeId)
                .flatMap(taskMapper::toDomain)
                .doOnSuccess(_ -> log.debug("Found task by assignee ID: {}", assigneeId))
                .onErrorMap(DataAccessException.class, ex ->
                        new DataReadingException("failed to found task with assignee ID: " + assigneeId, ex));
    }
}