package org.tasktracker.demo.userservice.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.mapper.UserMapper;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.domain.repository.UserRepository;
import org.tasktracker.demo.userservice.exception.DataAccessException;
import org.tasktracker.demo.userservice.exception.UserExistenceException;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:24:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserMapper userMapper;
    private final UserReactiveRepository reactiveRepository;

    @Override
    public Mono<User> findById(UUID id) {
        log.debug("Finding user by ID in repository: {}", id);
        return reactiveRepository.findById(id)
                .flatMap(userMapper::toDomain)
                .doOnSuccess(_ -> log.debug("Found user by ID: {}", id))
                .onErrorMap(DataAccessException.class, ex ->
                        new UserExistenceException("Failed to find user with id: " + id, ex));
    }

    @Override
    public Mono<User> findByUsername(String username) {
        log.debug("Finding user by username in repository: {}", username);
        return reactiveRepository.findByUsername(username)
                .flatMap(userMapper::toDomain)
                .doOnSuccess(_ -> log.debug("Found user by username: {}", username))
                .onErrorMap(DataAccessException.class, ex ->
                        new UserExistenceException("Failed to find user with username: " + username, ex));
    }

    @Override
    public Mono<User> save(User user) {
        log.debug("Saving user with ID: {}", user.getId());
        return Mono.just(user)
                .flatMap(userMapper::toEntity)
                .flatMap(reactiveRepository::save)
                .flatMap(userMapper::toDomain)
                .doOnSuccess(saved -> log.debug("Successfully saved user with ID: {}", saved.getId()))
                .onErrorMap(DataAccessException.class, ex ->
                        new UserExistenceException("Failed to save user with id: " + user.getId(), ex));
    }

    @Override
    public Mono<Boolean> existsByEmail(Email email) {
        log.debug("Checking email existence: {}", email.value());
        return reactiveRepository.existsByEmail(email.value())
                .doOnSuccess(exists -> log.debug("Email {} exists {}", email.value(), exists))
                .onErrorMap(DataAccessException.class, ex ->
                        new UserExistenceException("Failed to check email existence: " + email.value(), ex));
    }

    @Override
    public Mono<Void> delete(UUID id) {
        log.debug("Deleting user by ID in repository: {}", id);
        return reactiveRepository.deleteById(id)
                .doOnSuccess(_ -> log.debug("Successfully deleted user with ID: {}", id))
                .onErrorMap(DataAccessException.class, ex ->
                        new UserExistenceException("Failed to delete user with id:" + id, ex));
    }
}