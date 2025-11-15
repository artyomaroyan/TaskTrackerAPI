package org.tasktracker.demo.userservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:57:24
 */
public record UserRequest(
        @NotBlank(message = "username is required field!")
        @Size(message = "username must be between 8 - 20 characters", min = 8, max = 20)
        String username,
        @NotBlank(message = "email is required field!")
        @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        String email,
        @NotBlank(message = "password is required field!")
        @Size(message = "password must be between 8 - 20 characters", min = 8, max = 20)
        String password
) {
}