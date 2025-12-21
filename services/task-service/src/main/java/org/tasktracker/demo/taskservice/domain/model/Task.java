package org.tasktracker.demo.taskservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:31:52
 */
@Getter
@AllArgsConstructor(access = PRIVATE)
public class Task {
    private final UUID id;
    private final UUID assigneeId;
    private final String title;
    private final String description;
    private final Status status;
    private final Priority priority;
    private final Instant createdAt;
    private final Instant updatedAt;

    public static Task create(String title, String description, Status status, Priority priority) {
        return new Task(null, null, title, description, status, priority, Instant.now(), Instant.now());
    }

    public static Task of(UUID id, UUID assigneeId, String title, String description,
                          Status status, Priority priority, Instant createdAt, Instant updatedAt) {
        return new Task(id, assigneeId, title, description, status, priority, createdAt, updatedAt);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task that)) return false;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.assigneeId, that.assigneeId) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.status, that.status) &&
                Objects.equals(this.priority, that.priority) &&
                Objects.equals(this.createdAt, that.createdAt) &&
                Objects.equals(this.updatedAt, that.updatedAt);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, assigneeId, title, description, status, priority, createdAt, updatedAt);
    }
}