package org.tasktracker.demo.taskservice.infrastructure.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.tasktracker.demo.taskservice.domain.model.Status;
import org.tasktracker.demo.taskservice.domain.model.Task;
import org.tasktracker.demo.taskservice.infrastructure.persistence.entity.TaskEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:55:06
 */
public interface TaskReactiveRepository extends ReactiveCrudRepository<TaskEntity, UUID> {
    Mono<TaskEntity> findByTitle(String title);
    Mono<TaskEntity> findByAssigneeId(UUID assigneeId);
    Mono<TaskEntity> updateTask(UUID id, Task existing);
    Mono<TaskEntity> updateStatus(UUID id, Status newStatus);
}