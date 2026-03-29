package org.tasktracker.demo.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 23.11.25
 * Time: 23:50:52
 */
@Validated
public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}