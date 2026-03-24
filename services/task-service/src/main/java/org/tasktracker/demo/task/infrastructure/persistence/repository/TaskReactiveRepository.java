package org.tasktracker.demo.task.infrastructure.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.tasktracker.demo.task.infrastructure.persistence.entity.TaskEntity;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:55:06
 */
public interface TaskReactiveRepository extends ReactiveCrudRepository<TaskEntity, UUID> {
    Flux<TaskEntity> findByTitle(String title);

    @Query("SELECT t FROM tasks.tasks t WHERE" +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND" +
            "(:createAt IS NULL OR t.created_at = :createdAt) AND" +
            "(:dueDate IS NULL OR t.due_date = :dueDate)"
    )
    Flux<TaskEntity> findByFilter(String status, String priority, Instant createdAt, Instant dueDate);
    Flux<TaskEntity> findByAssigneeId(UUID assigneeId);
}