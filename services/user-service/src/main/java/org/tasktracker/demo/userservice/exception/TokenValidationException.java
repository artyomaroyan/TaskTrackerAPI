package org.tasktracker.demo.userservice.exception;

/**
 * Author: Artyom Aroyan
 * Date: 25.11.25
 * Time: 14:12:14
 */
public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message) {
        super(message);
    }
}