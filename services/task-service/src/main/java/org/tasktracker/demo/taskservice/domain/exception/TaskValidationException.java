package org.tasktracker.demo.taskservice.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 27.01.26
 * Time: 23:19:56
 */
public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
}