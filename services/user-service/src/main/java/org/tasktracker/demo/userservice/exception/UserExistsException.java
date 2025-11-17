package org.tasktracker.demo.userservice.exception;

/**
 * Author: Artyom Aroyan
 * Date: 16.11.25
 * Time: 15:25:31
 */
public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}