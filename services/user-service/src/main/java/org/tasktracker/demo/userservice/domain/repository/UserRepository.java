package org.tasktracker.demo.userservice.domain.repository;

import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:14:31
 */
public interface UserRepository {
    Mono<User> findById(UUID id);
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(Email email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Void> delete(UUID id);
}