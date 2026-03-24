package org.tasktracker.demo.task.domain.model;

import org.springframework.stereotype.Component;
import org.tasktracker.demo.task.application.dto.TaskRequest;
import org.tasktracker.demo.task.domain.exception.TaskValidationException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Author: Artyom Aroyan
 * Date: 23.03.26
 * Time: 14:37:14
 */
@Component
public class TaskValidator {
    private static final int MIN_DEADLINE_DAYS = 3;

    public Mono<TaskRequest> validate(TaskRequest request) {
        return Mono.just(request)
                .flatMap(this::validateTitle)
                .flatMap(this::validateDueDate);
    }

    private Mono<TaskRequest> validateTitle(TaskRequest request) {
        if (request.title() == null || request.title().trim().isEmpty()) {
            return Mono.error(new TaskValidationException("Title is required!"));
        }
        if (request.title().length() > 30) {
            return Mono.error(new TaskValidationException("Title must not exceed 30 characters"));
        }
        return Mono.just(request);
    }

    private Mono<TaskRequest> validateDueDate(TaskRequest request) {
        Instant minDueDate = Instant.now().plus(MIN_DEADLINE_DAYS, ChronoUnit.DAYS);
        if (request.dueDate().isBefore(minDueDate)) {
            return Mono.error(new TaskValidationException(
                    String.format("Task must have at least %d days deadline", MIN_DEADLINE_DAYS)));
        }
        return Mono.just(request);
    }
}