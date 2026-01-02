package org.tasktracker.demo.taskservice.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.01.26
 * Time: 22:07:18
 */
public class DataTransactionException extends RuntimeException {
    public DataTransactionException(String message, Throwable cause) {
        super(message);
    }
}