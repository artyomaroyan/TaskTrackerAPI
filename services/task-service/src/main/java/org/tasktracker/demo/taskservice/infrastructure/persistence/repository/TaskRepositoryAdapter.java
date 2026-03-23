package org.tasktracker.demo.taskservice.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.mapper.TaskMapper;
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
@Repository
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepository {
    private final TaskMapper taskMapper;
    private final TaskReactiveRepository reactiveRepository;

    @Override
    public Mono<Task> save(Task task) {
        return taskMapper.toEntity(task)
                .flatMap(reactiveRepository::save)
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Flux<Task> findAllTasks() {
        return reactiveRepository.findAll()
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Flux<Task> findTaskByTitle(String title) {
        return reactiveRepository.findByTitle(title)
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Flux<Task> findTasksByFilter(TaskFilter filter) {
        if (filter == null) {
            return this.findAllTasks();
        }
        return reactiveRepository.findByFilter(filter.status().name(), filter.priority().name(),
                        filter.createdAt(), filter.dueDate())
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Mono<Task> findTaskById(UUID taskId) {
        return reactiveRepository.findById(taskId)
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Flux<Task> findTaskByAssigneeId(UUID assigneeId) {
        return reactiveRepository.findByAssigneeId(assigneeId)
                .flatMap(taskMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteTask(UUID id) {
        return reactiveRepository.deleteById(id);
    }
}