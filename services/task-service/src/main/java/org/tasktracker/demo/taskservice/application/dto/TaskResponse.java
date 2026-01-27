package org.tasktracker.demo.taskservice.application.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 25.01.26
 * Time: 01:48:33
 */
public record TaskResponse(
        UUID id,
        UUID assigneeId,
        String title,
        String description,
        String status,
        String priority,
        Instant createdAt,
        Instant dueDate
) {
}