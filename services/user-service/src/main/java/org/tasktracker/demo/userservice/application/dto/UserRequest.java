package org.tasktracker.demo.userservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:57:24
 */
@Validated
public record UserRequest(
        @Schema(example = "user1")
        @NotBlank(message = "username is required field!")
        @Size(message = "username must be between 5 - 20 characters", min = 5, max = 20)
        String username,
        @Schema(example = "user1@gmail.com")
        @NotBlank(message = "email is required field!")
        String email,
        @Schema(example = "password1")
        @NotBlank(message = "password is required field!")
        @Size(message = "password must be between 8 - 20 characters", min = 8, max = 20)
        String password
) {
}