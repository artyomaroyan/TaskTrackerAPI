package org.tasktracker.demo.taskservice.application.dto;

import org.tasktracker.demo.taskservice.domain.model.Priority;

import java.time.Instant;

/**
 * Author: Artyom Aroyan
 * Date: 25.12.25
 * Time: 02:58:22
 */
public record TaskUpdateRequest(String title, String description, Priority priority, Instant dueDate) {
}