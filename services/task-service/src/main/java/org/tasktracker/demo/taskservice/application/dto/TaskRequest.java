package org.tasktracker.demo.taskservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @NotBlank(message = "Task must have a title")
        @Size(message = "Task title must be between 1 - 50 characters", min = 1, max = 50)
        String title,
        @NotBlank(message = "Task must have a description")
        @Size(message = "Task description must be between 1 - 500 characters", min = 1, max = 500)
        String description,
        @NotNull(message = "Please select task priority!")
        Priority priority,
        @NotNull(message = "Please set deadline for this task!")
        Instant dueDate) {
}