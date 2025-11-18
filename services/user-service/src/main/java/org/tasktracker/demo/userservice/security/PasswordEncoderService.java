package org.tasktracker.demo.userservice.security;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Author: Artyom Aroyan
 * Date: 16.11.25
 * Time: 15:14:35
 */
@Service
public class PasswordEncoderService implements PasswordEncoder {
    private final char[] pepper;
    private final Argon2PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncoderService(PasswordEncoderProperties properties, Argon2PasswordEncoder passwordEncoder) {
        this.pepper = properties.pepper();
        this.passwordEncoder = passwordEncoder;
    }

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
        Arrays.fill(pepper, '\0');
    }

    private String addPepper(CharSequence rawPassword) {
        return rawPassword + new String(pepper);
    }
}