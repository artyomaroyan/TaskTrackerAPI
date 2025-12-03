package org.tasktracker.demo.userservice.domain.model;

/**
 * Author: Artyom Aroyan
 * Date: 03.12.25
 * Time: 16:14:28
 */
public record Password(String value) {
    public Password {
        if (value == null || value.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}