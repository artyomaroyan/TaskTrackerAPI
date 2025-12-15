package org.tasktracker.demo.userservice.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Author: Artyom Aroyan
 * Date: 19.11.25
 * Time: 00:28:11
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class BeanConfiguration {

    @Bean
    protected Scheduler jwtScheduler() {
        return Schedulers.newBoundedElastic(
                10,
                100,
                "jwt-scheduler",
                60,
                true
        );
    }
}