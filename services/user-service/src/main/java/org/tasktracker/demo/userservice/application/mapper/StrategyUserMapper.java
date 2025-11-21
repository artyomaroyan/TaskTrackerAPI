/*
package org.tasktracker.demo.userservice.application.mapper;

import org.springframework.stereotype.Component;
import org.tasktracker.demo.mapper.strategy.StrategyMapper;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;

import java.util.function.BiFunction;

/**
 * Author: Artyom Aroyan
 * Date: 22.11.25
 * Time: 00:10:00
 * This class is example code, which I write just to not lose this mapping logic.
@Component
public class StrategyUserMapper implements StrategyMapper<User, UserEntity> {

    @Override
    public User toDomain(UserEntity entity, BiFunction<Class<?>, Object, Object> dependencyResolver) {
        return User.of(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                new Email(entity.getEmail()),
                Role.valueOf(entity.getRole()),
                entity.getCreatedAt()
        );
    }

    @Override
    public UserEntity toEntity(User domain, BiFunction<Class<?>, Object, Object> dependencyResolver) {
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .email(domain.getEmail().value())
                .role(domain.getRole().name())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
 */