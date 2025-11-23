package org.tasktracker.demo.userservice.infrastructure.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:26:07
 */
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, UUID> {
    Mono<Boolean> existsByEmail(String email);
    Mono<UserEntity> findByUsername(String username);
}