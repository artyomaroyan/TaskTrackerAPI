package org.tasktracker.demo.taskservice.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.tasktracker.demo.taskservice.domain.model.Priority;
import org.tasktracker.demo.taskservice.domain.model.Status;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:51:09
 */
@Getter
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "tasks", schema = "tasks")
public class TaskEntity {
    @Id
    private UUID id;
    private UUID assigneeId;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Instant createdAt;
    private Instant updatedAt;
}