package org.tasktracker.demo.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 19.11.25
 * Time: 00:28:11
 */
@Configuration
class BeanConfiguration {

    @Bean
    protected ReactiveUserDetailsService userDetailsService() {
        return _ -> Mono.empty();
    }
}