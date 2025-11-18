package org.tasktracker.demo.userservice.security;

import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 16.11.25
 * Time: 14:54:44
 */
@Validated
@ConfigurationProperties("spring.security.argon2")
public record PasswordEncoderProperties(
        @Positive int memory,
        @Positive int iterations,
        @Positive int parallelism,
        @Positive int hashLength,
        @Positive int saltLength,
        char @NonNull [] pepper
) {
}