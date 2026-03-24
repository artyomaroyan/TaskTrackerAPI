package org.tasktracker.demo.user.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.tasktracker.demo.user.security.PasswordEncoderService;

/**
 * Author: Artyom Aroyan
 * Date: 16.11.25
 * Time: 14:59:13
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PasswordEncoderProperties.class)
public class PasswordEncoderConfiguration {
    private final PasswordEncoderProperties properties;

    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(
                properties.saltLength(),
                properties.hashLength(),
                properties.parallelism(),
                properties.memory(),
                properties.iterations()
        );
    }

    @Bean
    public PasswordEncoderService passwordEncoderService(Argon2PasswordEncoder passwordEncoder) {
        return new PasswordEncoderService(properties, passwordEncoder);
    }
}