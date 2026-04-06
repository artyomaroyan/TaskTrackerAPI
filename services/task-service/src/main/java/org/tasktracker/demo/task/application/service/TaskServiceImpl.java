package org.tasktracker.demo.task.application.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasktracker.demo.task.application.dto.TaskFilter;
import org.tasktracker.demo.task.application.dto.TaskRequest;
import org.tasktracker.demo.task.application.dto.TaskResponse;
import org.tasktracker.demo.task.application.mapper.TaskMapper;
import org.tasktracker.demo.task.application.port.in.TaskService;
import org.tasktracker.demo.task.domain.exception.DataNotFoundException;
import org.tasktracker.demo.task.domain.exception.DataSavingException;
import org.tasktracker.demo.task.domain.exception.TaskValidationException;
import org.tasktracker.demo.task.domain.model.Status;
import org.tasktracker.demo.task.domain.model.Task;
import org.tasktracker.demo.task.domain.model.TaskValidator;
import org.tasktracker.demo.task.application.port.out.TaskRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

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
    private final TaskValidator taskValidator;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CREATE')")
    public Mono<TaskResponse> createTask(@Valid TaskRequest request, UUID userId) {
        log.info("Creating task for user: {}, title: {}", userId, request.title());

        return taskValidator.validate(request)
                .flatMap(validated -> {
                    try {
                        Task task = Task.create(
                                userId,
                                validated.title(),
                                validated.description(),
                                validated.priority(),
                                validated.dueDate()
                        );
                        return taskRepository.save(task)
                                .map(taskMapper::toResponse)
                                .doOnSuccess(response -> log.info("Task created: {}", response.id()))
                                .doOnError(error -> log.error("Failed to create task for user: {}", userId, error));
                    } catch (DataSavingException ex) {
                        return Mono.error(() -> new DataSavingException("Failed to save task"));
                    }
                });
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public Mono<TaskResponse> updateTask(UUID taskId, UUID userId, TaskRequest request) {
        log.info("Updating task by title: {}", request.title());

        return taskRepository.findTaskById(taskId)
                .switchIfEmpty(Mono.error(new DataNotFoundException("Task " + taskId + " not found")))
                .flatMap(task -> {
                    if (!task.assigneeId().equals(userId)) {
                        log.warn("User {} attempted to update task {} owned by {}", userId, taskId, task.assigneeId());
                        return Mono.error(new AccessDeniedException("You do not have permission to update this task"));
                    }

                    if (task.status() == Status.DONE) {
                        return Mono.error(new TaskValidationException("Cannot update a completed task"));
                    }

                    final Task updatedTask;
                    try {
                        updatedTask = task.updateTask(request.title(), request.description(), request.priority(), request.dueDate());
                    } catch (IllegalArgumentException ex) {
                        return Mono.error(new TaskValidationException(ex.getMessage()));
                    }
                    return taskRepository.save(updatedTask);
                })
                .map(taskMapper::toResponse)
                .doOnSuccess(_ -> log.info("Task updated successfully: {}", taskId))
                .doOnError(error -> log.error("Failed to update task: {}", taskId, error));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('UPDATE', 'UPDATE_SELF')")
    public Mono<TaskResponse> changeStatus(UUID taskId, UUID userId, Status status) {
        log.info("prepare to update task: {}", taskId);

        return taskRepository.findTaskById(taskId)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("No task was found with %s ID:", taskId))))
                .flatMap(task -> {
                    if (!task.assigneeId().equals(userId)) {
                        log.warn("User {} attempted to update status of task {} owned by {}", userId, taskId, task.assigneeId());
                        return Mono.error(new AccessDeniedException("You do not have permission to update this task"));
                    }

                    if (task.status().equals(status)) {
                        return Mono.error(new TaskValidationException("Task is already on stats " + status.name()));
                    }

                    final Task updated;
                    try {
                        updated = task.changeStatus(status);
                    } catch (IllegalArgumentException ex) {
                        return Mono.error(new TaskValidationException(ex.getMessage()));
                    }
                    return taskRepository.save(updated);
                })
                .map(taskMapper::toResponse)
                .doOnSuccess(task -> log.info("Task status has successfully changed to - {}", task.status()))
                .doOnError(error -> log.error("Failed to update task status: {}", taskId, error));

//        return taskRepository.findTaskById(taskId)
//                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("No task was found with %s ID:", taskId))))
//                .map(task -> task.changeStatus(status))
//                .flatMap(taskRepository::save)
//                .map(taskMapper::toResponse)
//                .doOnSuccess(_ -> log.info("Task {} status changed to {}", taskId, status))
//                .doOnError(error -> log.error("Failed to update task status: {}", taskId, error));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')") // also need to chack if the user who make the request is the logged-in user.
    public Flux<TaskResponse> findAllTasks() {
        log.info("prepare to get all tasks from DB");

        return taskRepository.findAllTasks()
                .switchIfEmpty(Mono.error(new DataNotFoundException("Can not find any task")))
                .map(taskMapper::toResponse)
                .doOnComplete(() -> log.info("Successfully fetched tasks from DB"))
                .doOnError(error -> log.error("Failed to fetch tasks from DB", error));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<TaskResponse> findAllTasksByStatus(UUID userId, Status status) {
        log.info("prepare to find all tasks with specified status");

        return taskRepository.findAllTasks()
                .switchIfEmpty(Mono.error(new DataNotFoundException("Can not find any task")))
                .filter(task -> task.status().equals(status))
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("Task with %s status was not found", status.name()))))
                .map(taskMapper::toResponse)
                .doOnComplete(() -> log.info("Successfully fetched task(s) with status {}", status));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<TaskResponse> findTaskById(UUID userId, UUID taskId) {
        log.info("prepare to find task by ID");

        return taskRepository.findTaskById(taskId)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("No task was found with %s ID ", taskId))))
                .flatMap(task -> {
                    if (!task.assigneeId().equals(userId)) {
                        log.warn("User {} attempted to find task {}, permission denied:", userId, taskId);
                        return Mono.error(new AccessDeniedException("You do not have permission to fetch task by ID: LOOSER :)"));
                    }
                    return Mono.just(taskMapper.toResponse(task));
                })
                .doOnSuccess(_ -> log.info("Fetched task with {} ID", taskId))
                .doOnError(error -> log.error("Failed to found task with {} ID", taskId, error));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<TaskResponse> findTaskByAssigneeId(UUID assigneeId) {
        log.info("prepare to find task with assigneeId");

        return taskRepository.findTaskByAssigneeId(assigneeId)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("Task with %s assigneeId not found", assigneeId))))
                .filter(task -> task.assigneeId().equals(assigneeId))
                .map(taskMapper::toResponse)
                .doOnComplete(() -> log.info("Successfully fetched task with {} assigneeId", assigneeId))
                .doOnError(error -> log.error("Failed to find task with {} assigneeId", assigneeId, error));
    }

    @Override
    @PreAuthorize("hasAuthority('READ')")
    public Flux<TaskResponse> findTaskByTitle(String title) {
        log.info("prepare to find task by title");

        return taskRepository.findTaskByTitle(title)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("Task with %s title not found", title))))
                .filter(task -> task.title().equals(title))
                .map(taskMapper::toResponse)
                .doOnComplete(() -> log.info("Successfully fetched tasks with {} title", title))
                .doOnError(error -> log.error("Failed to find task with {} title", title, error));
    }

    @Override
    public Flux<TaskResponse> findTaskByFilter(TaskFilter filter) {
        log.info("prepare to find task with filter");

        return taskRepository.findTasksByFilter(filter)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("Task with %s filter not found", filter))))
                .map(taskMapper::toResponse)
                .doOnComplete(() -> log.info("Successfully fetched tasks with filter {}", filter))
                .doOnError(error -> log.error("Failed to found tasks with filter {}", filter, error));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasPermission('DELETE')")
    public Mono<Void> deleteTask(UUID taskId) {
        log.info("prepare to delete task with ID");

        return taskRepository.findTaskById(taskId)
                .switchIfEmpty(Mono.error(new DataNotFoundException(String.format("Task with %s ID not found", taskId))))
                .flatMap(task -> taskRepository.deleteTask(task.id()))
                .doOnSuccess(_ -> log.info("Task {} successfully deleted", taskId))
                .doOnError(error -> log.error("Failed to delete {} task", taskId, error));
    }
}