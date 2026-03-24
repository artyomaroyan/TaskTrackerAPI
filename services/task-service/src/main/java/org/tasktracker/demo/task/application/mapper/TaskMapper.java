package org.tasktracker.demo.task.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.exception.DataMappingException;
import org.tasktracker.demo.mapper.BaseMapper;
import org.tasktracker.demo.task.application.dto.TaskResponse;
import org.tasktracker.demo.task.domain.model.Priority;
import org.tasktracker.demo.task.domain.model.Task;
import org.tasktracker.demo.task.infrastructure.persistence.entity.TaskEntity;

/**
 * Author: Artyom Aroyan
 * Date: 23.12.25
 * Time: 20:42:13
 */
@Slf4j
@Component
public class TaskMapper extends BaseMapper<Task, TaskEntity> {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.id(),
                task.assigneeId(),
                task.title(),
                task.description(),
                task.status().name(),
                task.priority().name(),
                task.createdAt(),
                task.dueDate()
        );
    }

    @Override
    protected Task mapToDomain(TaskEntity entity) {
        try {
            return Task.of(
                    entity.getId(),
                    entity.getAssigneeId(),
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getStatus(),
                    Priority.valueOf(entity.getPriority()),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt(),
                    entity.getDueDate());
        } catch (MappingException ex) {
            log.debug("Invalid mapping parameters for task. {}", ex.getMessage(), ex);
            throw new DataMappingException("Invalid mapping parameters.", ex.getCause());
        }
    }

    @Override
    protected TaskEntity mapToEntity(Task domain) {
        return TaskEntity.builder()
                .id(domain.id())
                .assigneeId(domain.assigneeId())
                .title(domain.title())
                .description(domain.description())
                .status(domain.status())
                .priority(domain.priority().name())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .dueDate(domain.dueDate())
                .build();
    }
}