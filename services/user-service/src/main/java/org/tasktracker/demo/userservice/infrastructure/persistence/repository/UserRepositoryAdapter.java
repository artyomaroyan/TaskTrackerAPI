package org.tasktracker.demo.userservice.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.domain.repository.UserRepository;
import org.tasktracker.demo.userservice.exception.DataAccessException;
import org.tasktracker.demo.userservice.exception.UserPersistenceException;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:24:31
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserReactiveRepository reactiveRepository;

    @Override
    public Mono<User> findById(UUID id) {
        return reactiveRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.just(user)
                .map(this::toEntity)
                .flatMap(reactiveRepository::save)
                .map(this::toDomain)
                .onErrorMap(DataAccessException.class, ex -> new UserPersistenceException("Failed to persist user", ex));
    }

    @Override
    public Mono<Boolean> existsByEmail(Email email) {
        return reactiveRepository.existsByEmail(email.value());
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return reactiveRepository.existsByUsername(username);
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return reactiveRepository.deleteById(id);
    }

    private User toDomain(UserEntity entity) {
        return User.of(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                new Email(entity.getEmail()),
                Role.valueOf(entity.getRole()),
                entity.getCreatedAt());
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail().value())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}