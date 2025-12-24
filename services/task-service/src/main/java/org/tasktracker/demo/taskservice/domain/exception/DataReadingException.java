package org.tasktracker.demo.taskservice.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 24.12.25
 * Time: 01:10:00
 */
public class DataReadingException extends RuntimeException {
    public DataReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}