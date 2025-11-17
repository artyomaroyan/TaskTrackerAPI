package org.tasktracker.demo.userservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.tasktracker.demo.userservice.domain.model.Email;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:57:24
 */
@Validated
public record UserRequest(
        @NotBlank(message = "username is required field!")
        @Size(message = "username must be between 8 - 20 characters", min = 8, max = 20)
        String username,
        @NotBlank(message = "email is required field!")
        Email email,
        @NotBlank(message = "password is required field!")
        @Size(message = "password must be between 8 - 20 characters", min = 8, max = 20)
        String password
) {
}