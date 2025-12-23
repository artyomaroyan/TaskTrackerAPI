package org.tasktracker.demo.taskservice.application.exception;

/**
 * Author: Artyom Aroyan
 * Date: 24.12.25
 * Time: 00:22:14
 */
public class DataReadException extends RuntimeException {
    public DataReadException(String message) {
        super(message);
    }
}