package org.tasktracker.demo.userservice.exception;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 18:25:55
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}