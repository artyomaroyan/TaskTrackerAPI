package org.tasktracker.demo.taskservice.infrastructure.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.tasktracker.demo.taskservice.infrastructure.persistence.entity.TaskEntity;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 21:55:06
 */
public interface TaskReactiveRepository extends ReactiveCrudRepository<TaskEntity, UUID> {
}