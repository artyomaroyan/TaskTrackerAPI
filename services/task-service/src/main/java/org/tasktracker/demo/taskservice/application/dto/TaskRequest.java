package org.tasktracker.demo.taskservice.application.dto;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.tasktracker.demo.taskservice.domain.model.Priority;

import java.time.Instant;

/**
 * Author: Artyom Aroyan
 * Date: 23.12.25
 * Time: 20:28:07
 */
@Validated
public record TaskRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 50, message = "Title must be between 1 and 30 characters")
        @Pattern(regexp = "^[A-Za-z0-9\\s\\-_,.!?]+$", message = "Title contains invalid characters")
        String title,
        @Size(max = 500, message = "Description too long")
        String description,
        @NotNull(message = "Priority is required")
        Priority priority,
        @NotNull(message = "Due date is required")
        @Future(message = "Due date must be in the future")
        Instant dueDate) {
}