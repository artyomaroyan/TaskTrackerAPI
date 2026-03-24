package org.tasktracker.demo.task.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 01.01.26
 * Time: 21:33:13
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}