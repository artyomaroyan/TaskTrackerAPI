package org.tasktracker.demo.taskservice.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
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

    public Task updateTask(UUID assigneeId, String title, String description, Status status, Priority priority, Instant dueDate) {
        return new Task(
                this.id,
                Optional.ofNullable(assigneeId).orElse(this.assigneeId),
                Optional.ofNullable(title).orElse(this.title),
                Optional.ofNullable(description).orElse(this.description),
                Optional.ofNullable(status).orElse(this.status),
                Optional.ofNullable(priority).orElse(this.priority),
                this.createdAt,
                Instant.now(),
                Optional.ofNullable(dueDate).orElse(this.dueDate)
        );
    }

    public Task changeStatus(Status newStatus) {
        return new Task(this.id, this.assigneeId, this.title, this.description,
                newStatus, this.priority, this.createdAt, Instant.now(), this.dueDate);
    }

    public static Task of(UUID id, UUID assigneeId, String title, String description, Status status,
                          Priority priority, Instant createdAt, Instant updatedAt, Instant dueDate) {
        return new Task(id, assigneeId, title, description, status, priority, createdAt, updatedAt, dueDate);
    }
}