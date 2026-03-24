package org.tasktracker.demo.user.application.ports.out;

import org.tasktracker.demo.user.domain.model.Email;
import org.tasktracker.demo.user.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:14:31
 */
public interface UserRepository {
    Mono<User> save(User user);
    Mono<User> findById(UUID id);
    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByEmail(Email email);
    Mono<Void> delete(UUID id);
}