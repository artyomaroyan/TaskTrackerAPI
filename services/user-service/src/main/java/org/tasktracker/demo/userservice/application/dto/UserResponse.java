package org.tasktracker.demo.userservice.application.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 07.12.25
 * Time: 18:38:51
 */
public record UserResponse(
        UUID id,
        String username,
        String email,
        Set<String> role,
        Instant createdAt,
        boolean active
) {
}