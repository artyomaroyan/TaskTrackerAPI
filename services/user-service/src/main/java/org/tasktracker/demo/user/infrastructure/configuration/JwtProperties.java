package org.tasktracker.demo.user.infrastructure.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:44:14
 */
@Validated
@ConfigurationProperties("spring.security.jwt")
public record JwtProperties(
        @NotBlank String issuer,
        @Positive long expiration,
        @NotBlank String algorithm,
        @NotBlank String rsaPrivateKeyPath,
        @NotBlank String rsaPublicKeyPath
) {
}