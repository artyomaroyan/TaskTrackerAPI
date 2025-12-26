package org.tasktracker.demo.taskservice.application.usecases;

import org.tasktracker.demo.taskservice.application.dto.TaskFilter;
import org.tasktracker.demo.taskservice.application.dto.TaskRequest;
import org.tasktracker.demo.taskservice.application.dto.TaskUpdateRequest;
import org.tasktracker.demo.taskservice.application.ports.in.TaskService;
import org.tasktracker.demo.taskservice.domain.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:37:45
 */
public class TaskServiceImpl implements TaskService {

    @Override
    public Mono<Task> createTask(TaskRequest request) {
        return null;
    }

    @Override
    public Flux<Task> listUsersTasks(TaskFilter filter) {
        return null;
    }

    @Override
    public Mono<Task> findTaskById(UUID id) {
        return null;
    }

    @Override
    public Mono<Task> updateTask(UUID id, TaskUpdateRequest request) {
        return null;
    }

    @Override
    public Mono<Task> changeStatus(UUID id, String status) {
        return null;
    }

    @Override
    public Mono<Void> deleteTask(UUID id) {
        return null;
    }
}