package org.tasktracker.demo.user.domain.exception;

/**
 * Author: Artyom Aroyan
 * Date: 20.11.25
 * Time: 14:26:56
 */
public class UserExistenceException extends RuntimeException {
    public UserExistenceException(String message) {
        super(message);
    }

    public UserExistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}