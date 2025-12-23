package org.tasktracker.demo.taskservice.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 24.12.25
 * Time: 00:18:23
 */
public class DataSavingException extends RuntimeException {
    public DataSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}