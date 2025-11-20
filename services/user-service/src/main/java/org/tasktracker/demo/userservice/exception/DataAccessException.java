package org.tasktracker.demo.userservice.exception;

/**
 * Author: Artyom Aroyan
 * Date: 20.11.25
 * Time: 14:27:39
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }
}