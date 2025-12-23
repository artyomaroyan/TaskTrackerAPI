package org.tasktracker.demo.taskservice.application.exception;

/**
 * Author: Artyom Aroyan
 * Date: 24.12.25
 * Time: 00:16:43
 */
public class DataWritingException extends RuntimeException {
    public DataWritingException(String message) {
        super(message);
    }
}