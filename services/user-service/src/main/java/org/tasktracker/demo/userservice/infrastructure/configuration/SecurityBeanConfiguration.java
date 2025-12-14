package org.tasktracker.demo.userservice.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * Author: Artyom Aroyan
 * Date: 19.11.25
 * Time: 00:28:11
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityBeanConfiguration {

//    @Bean
//    @Primary
//    protected ReactiveAuthenticationManager jwtAuthenticationManager(JwtReactiveAuthenticationManager manager) {
//        return new DelegatingReactiveAuthenticationManager(manager);
//    }
//
//    @Bean
//    protected JwtReactiveAuthenticationManager(ReactiveJwtDecoder decoder) {
//        return new JwtReactiveAuthenticationManager(decoder);
//    }
//
//    @Bean
//    protected AuthenticationWebFilter jwtAuthenticationFilter() {
//        return new AuthenticationWebFilter(jwtAuthenticationManager(jwtReactiveAuthenticationManager(jwtDecoder())));
//    }
//
//    @Bean
//    protected ReactiveJwtDecoder jwtDecoder() {
//        return null;
//    }
//
//    @Bean
//    protected ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
//        return null;
//    }
}