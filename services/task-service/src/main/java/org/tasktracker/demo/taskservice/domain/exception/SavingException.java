package org.tasktracker.demo.taskservice.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 24.12.25
 * Time: 00:18:23
 */
public class SavingException extends RuntimeException {
    public SavingException(String message, Throwable cause) {
        super(message, cause);
    }
}