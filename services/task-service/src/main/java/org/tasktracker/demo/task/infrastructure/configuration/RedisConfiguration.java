package org.tasktracker.demo.task.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.tasktracker.demo.task.application.dto.TaskResponse;

/**
 * Author: Artyom Aroyan
 * Date: 07.04.26
 * Time: 16:14:49
 */
@Configuration
public class RedisConfiguration {

    @Bean
    ReactiveRedisTemplate<String, TaskResponse> redisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        Jackson2JsonRedisSerializer<TaskResponse> serializer = new Jackson2JsonRedisSerializer<>(TaskResponse.class);
        RedisSerializationContext<String, TaskResponse> context = RedisSerializationContext.
                <String, TaskResponse>newSerializationContext(new StringRedisSerializer())
                .value(serializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}