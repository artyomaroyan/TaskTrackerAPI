package org.tasktracker.demo.taskservice.domain.model;

import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:31:52
 */
public record Task(UUID id, UUID assigneeId, String title, String description, Status status,
                   Priority priority, Instant createdAt, Instant updatedAt, Instant dueDate) {

    public Task(UUID id, UUID assigneeId, String title, String description, Status status,
                Priority priority, Instant createdAt, Instant updatedAt, Instant dueDate) {
        this.id = id;
        this.assigneeId = Objects.requireNonNull(assigneeId, "Assignee ID can not be null!");
        this.title = Objects.requireNonNull(title, "Title can not be null!");
        this.description = Objects.requireNonNull(description, "Description can not be null!");
        this.status = Objects.requireNonNull(status, "Status can not be null!");
        this.priority = Objects.requireNonNull(priority, "Priority can not be null!");
        this.createdAt = Objects.requireNonNull(createdAt, "Created date can not be null!");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated date can not be null!");
        this.dueDate = Objects.requireNonNull(dueDate, "Due date can not be null!");

    }

    public static Task create(UUID assigneeId, String title, String description, Status status, Priority priority, Instant dueDate) {
        return new Task(null, assigneeId, title, description, status, priority, Instant.now(), Instant.now(), dueDate);
    }

    public Task updateTask(@Nullable UUID assigneeId,
                           @Nullable String title,
                           @Nullable String description,
                           @Nullable Status status,
                           @Nullable Priority priority,
                           @Nullable Instant dueDate) {
        return new Task(
                this.id,
                assigneeId != null ? assigneeId : this.assigneeId,
                title != null ? title : this.title,
                description != null ? description : this.description,
                status != null ? status : this.status,
                priority != null ? priority : this.priority,
                this.createdAt,
                Instant.now(),
                dueDate != null ? dueDate : this.dueDate);
    }

    public static Task changeStatus(Task existing, Status newStatus) {
        return new Task(existing.id, existing.assigneeId, existing.title, existing.description,
                newStatus, existing.priority, existing.createdAt, Instant.now(), existing.dueDate);
    }

    public static Task of(UUID id, UUID assigneeId, String title, String description, Status status,
                          Priority priority, Instant createdAt, Instant updatedAt, Instant dueDate) {
        return new Task(id, assigneeId, title, description, status, priority, createdAt, updatedAt, dueDate);
    }
}