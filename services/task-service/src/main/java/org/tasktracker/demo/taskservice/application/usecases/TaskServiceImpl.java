package org.tasktracker.demo.taskservice.application.usecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskResponse;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.application.mapper.TaskMapper;
import org.tasktracker.demo.taskservice.application.ports.in.TaskService;
import org.tasktracker.demo.taskservice.domain.exception.DataNotFoundException;
import org.tasktracker.demo.taskservice.domain.exception.DataSavingException;
import org.tasktracker.demo.taskservice.domain.exception.DataTransactionException;
import org.tasktracker.demo.taskservice.domain.exception.TaskValidationException;
import org.tasktracker.demo.taskservice.domain.model.Status;
import org.tasktracker.demo.taskservice.domain.model.Task;
import org.tasktracker.demo.taskservice.domain.repository.TaskRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:37:45
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public Mono<TaskResponse> createTask(@Valid TaskRequest request, UUID userId) {
        return validateTaskRequest(request)
                .flatMap(_ -> createAndSaveTask(request, userId))
                .onErrorMap(DataTransactionException.class, ex ->
                        new DataTransactionException("Failed to create task.", ex.getCause()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public Mono<TaskResponse> updateTask(UUID id, TaskUpdateRequest request) {
        return taskRepository.findTaskById(id)
                .map(task -> task.updateTask(
                        request.title(),
                        request.description(),
                        request.priority(),
                        request.dueDate()
                ))
                .map(taskMapper::toResponse)
                .doOnSuccess(_ -> log.debug("Task successfully updated."))
                .onErrorMap(DataTransactionException.class, ex ->
                        new DataTransactionException("Failed to update task.", ex));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public Mono<TaskResponse> changeStatus(UUID id, Status status) {
        return taskRepository.findTaskById(id)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Task not found:" + id)))
                .map(task -> task.changeStatus(status))
                .flatMap(taskRepository::save)
                .map(taskMapper::toResponse)
                .doOnSuccess(_ -> log.debug("Task {} status changed to {}", id, status))
                .onErrorMap(DataTransactionException.class, ex ->
                        new DataTransactionException("Failed to change task status", ex));
    }

    @Override
    @PreAuthorize("hasRole('USER')") // also need to chack if the user who make the request is the logged-in user.
    public Flux<TaskResponse> listUsersTasks(TaskFilter filter) {
        return taskRepository.findAllTasks(filter)
                .map(taskMapper::toResponse)
                .switchIfEmpty(Mono.error(new DataNotFoundException("No task was founded.")));
    }

    @Override
    @PreAuthorize("hasRole('USER')") // also need to chack if the user who make the request is the logged-in user.
    public Mono<TaskResponse> findTaskById(UUID id) {
        return taskRepository.findTaskById(id)
                .map(taskMapper::toResponse)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Task not found with id " + id)));
    }

    @Override
    @PreAuthorize("hasRole('USER')") // also need to chack if the user who make the request is the logged-in user.
    public Flux<TaskResponse> findTaskByAssigneeId(UUID assigneeId) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Mono<TaskResponse> findTaskByTitle(String title) {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasPermission('DELETE')")
    public Mono<Void> deleteTask(UUID id) {
        return null;
    }

    // todo: improve validation logic.
    private Mono<TaskRequest> validateTaskRequest(TaskRequest request) {
        // 1. validate title
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\\s]+$");
        if (!pattern.matcher(request.title()).matches()) {
            return Mono.error(() -> new TaskValidationException("Title is invalid"));
        }

        // 2. validate date
        if (request.dueDate().isBefore(Instant.now())) {
            return Mono.error(() -> new TaskValidationException("Task must have minimum 3 days deadline"));
        }

        return Mono.just(request);
    }

    private Mono<TaskResponse> createAndSaveTask(TaskRequest request, UUID userId) {
        try {
            Task task = Task.create(userId, request.title(), request.description(), request.priority(),request.dueDate());
            return taskRepository.save(task)
                    .map(taskMapper::toResponse);
        } catch (DataSavingException ex) {
            throw new DataSavingException("Failed to create task");
        }
    }
}