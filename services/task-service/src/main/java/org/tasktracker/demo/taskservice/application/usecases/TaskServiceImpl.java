package org.tasktracker.demo.taskservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.application.ports.in.TaskService;
import org.tasktracker.demo.taskservice.domain.exception.DataSavingException;
import org.tasktracker.demo.taskservice.domain.model.Status;
import org.tasktracker.demo.taskservice.domain.model.Task;
import org.tasktracker.demo.taskservice.domain.repository.TaskRepository;
import org.tasktracker.demo.taskservice.security.SecurityContextService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Mono<Task> createTask(TaskRequest request) {
        return this.saveTask(request);
    }

    @Override
    @Transactional
    public Mono<Task> updateTask(UUID id, TaskUpdateRequest request) {
        return this.update(id, request);
    }

    @Override
    public Flux<Task> listUsersTasks(TaskFilter filter) {
        return taskRepository.findAllTasks();
    }

    @Override
    public Mono<Task> findTaskById(UUID id) {
        return taskRepository.findTaskById(id);
    }

    @Override
    public Mono<Task> findTaskByAssigneeId(UUID assigneeId) {
        return null;
    }

    @Override
    public Mono<Task> findTaskByTitle(String title) {
        return null;
    }

    @Override
    @Transactional
    public Mono<Task> changeStatus(UUID id, String status) {
        return null;
    }

    @Override
    @Transactional
    public Mono<Void> deleteTask(UUID id) {
        return null;
    }

    private Mono<Task> saveTask(TaskRequest request) {
        try {
            return SecurityContextService.currentUserId()
                    .flatMap(assigneeId -> {
                        Task newTask = Task.create(
                                assigneeId,
                                request.title(),
                                request.description(),
                                Status.NEW,
                                request.priority(),
                                request.dueDate()
                        );
                        return taskRepository.save(newTask);
                    });
        } catch (DataSavingException ex) {
            log.debug("Failed to save task. {}, {}", request.title(), ex.getMessage());
            throw new DataSavingException("unable to save task.", ex.getCause());
        }
    }

    private Mono<Task> update(UUID id, TaskUpdateRequest request) {
        try {
            return this.findTaskById(id)
                    .flatMap(currentTask -> {
                        Task updated = Task.updateTask(
                                currentTask,
                                UUID.randomUUID(),
                                request.title(),
                                request.description(),
                                Status.IN_PROGRESS,
                                request.priority(),
                                request.dueDate()
                        );
                        return taskRepository.updateTask(id, updated);
                    });
        } catch (DataSavingException ex) {
            log.debug("unable to update task. {}, {}", request.title(), ex.getMessage());
            throw new DataSavingException("task updating exception", ex.getCause());
        }
    }
}