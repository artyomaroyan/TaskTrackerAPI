package org.tasktracker.demo.taskservice.application.dto;

import org.tasktracker.demo.taskservice.domain.model.Priority;
import org.tasktracker.demo.taskservice.domain.model.Status;

import java.time.Instant;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:45:44
 */
public record TaskFilter(Status status, Priority priority, Instant createdAt, Instant dueDate) {
}