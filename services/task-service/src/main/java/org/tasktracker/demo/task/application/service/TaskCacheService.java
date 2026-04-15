package org.tasktracker.demo.task.application.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.task.application.dto.TaskResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 07.04.26
 * Time: 16:33:27
 */
@Service
public class TaskCacheService {
    private final ReactiveRedisTemplate<String, TaskResponse> redisTemplate;

    public TaskCacheService(ReactiveRedisTemplate<String, TaskResponse> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String KEY = "task";

    public Mono<TaskResponse> get(UUID id) {
        return redisTemplate.opsForValue()
                .get(KEY + id);
    }

    public Mono<Boolean> save(TaskResponse response) {
        return redisTemplate.opsForValue()
                .set(KEY + response.id(), response, Duration.ofMinutes(10));
    }

    public Mono<Boolean> delete(UUID id) {
        return redisTemplate.opsForValue()
                .delete(KEY + id);
    }
}