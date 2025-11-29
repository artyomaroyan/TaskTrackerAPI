package org.tasktracker.demo.userservice.security;

import jakarta.annotation.PreDestroy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tasktracker.demo.userservice.infrastructure.configuration.PasswordEncoderProperties;

import java.util.Arrays;

/**
 * Author: Artyom Aroyan
 * Date: 16.11.25
 * Time: 15:14:35
 */
public record PasswordEncoderService(PasswordEncoderProperties properties,
                                     Argon2PasswordEncoder passwordEncoder) implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(addPepper(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(addPepper(rawPassword), encodedPassword);
    }

    @PreDestroy
    private void clearPepper() {
        Arrays.fill(properties.pepper(), '\0');
    }

    private String addPepper(CharSequence rawPassword) {
        return rawPassword + new String(properties.pepper());
    }
}