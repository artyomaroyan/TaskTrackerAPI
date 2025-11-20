package org.tasktracker.demo.userservice.exception;

/**
 * Author: Artyom Aroyan
 * Date: 20.11.25
 * Time: 14:03:54
 */
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}