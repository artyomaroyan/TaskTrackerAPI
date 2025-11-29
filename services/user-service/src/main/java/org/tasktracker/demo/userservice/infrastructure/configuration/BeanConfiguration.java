package org.tasktracker.demo.userservice.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import org.tasktracker.demo.userservice.security.BasicAuthenticationManager;
import org.tasktracker.demo.userservice.security.DelegatingReactiveAuthenticationManager;
import org.tasktracker.demo.userservice.security.JwtAuthenticationManager;
import org.tasktracker.demo.userservice.security.MultipleAuthenticationConverter;

/**
 * Author: Artyom Aroyan
 * Date: 19.11.25
 * Time: 00:28:11
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class BeanConfiguration {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService userDetailsService;

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new DelegatingReactiveAuthenticationManager(
                basicAuthenticationManager(),
                jwtAuthenticationManager()
        );
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        return new JwtAuthenticationManager(tokenProvider);
    }

    @Bean
    public BasicAuthenticationManager basicAuthenticationManager() {
        return new BasicAuthenticationManager(passwordEncoder, userDetailsService);
    }

    @Bean
    public MultipleAuthenticationConverter multipleAuthenticationConverter() {
        return new MultipleAuthenticationConverter();
    }
}