package org.tasktracker.demo.taskservice.application.dto;

import jakarta.annotation.Nullable;
import org.tasktracker.demo.taskservice.domain.model.Priority;
import org.tasktracker.demo.taskservice.domain.model.Status;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:58:22
 */
public record TaskUpdateRequest(
        @Nullable String title,
        @Nullable String description,
        @Nullable Priority priority,
        @Nullable Instant dueDate) {
}