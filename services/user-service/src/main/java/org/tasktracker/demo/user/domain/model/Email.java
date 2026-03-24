package org.tasktracker.demo.user.domain.model;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:01:49
 */
public record Email(String value) {
    public Email {
        if (value == null || !isValidEmail(value)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    private static boolean isValidEmail(String value) {
        return value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}