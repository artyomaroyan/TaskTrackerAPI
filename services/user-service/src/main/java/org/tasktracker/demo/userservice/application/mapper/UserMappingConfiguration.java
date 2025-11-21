/*
package org.tasktracker.demo.userservice.application.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.mapper.functional.FunctionalMapper;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;

import java.util.function.Function;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 23:53:00
 * This class is example code, which I write just to not lose this mapping logic.

@Component
@RequiredArgsConstructor
public class UserMappingConfiguration {
    private final FunctionalMapper functionalMapper;

    public Function<UserEntity, User> userToDomainMapper() {
        return functionalMapper.createMapper(
                entity -> User.of(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getPassword(),
                        new Email(entity.getEmail()),
                        Role.valueOf(entity.getRole()),
                        entity.getCreatedAt()
                ),
                domain -> UserEntity.builder()
                        .id(domain.getId())
                        .username(domain.getUsername())
                        .password(domain.getPassword())
                        .email(domain.getEmail().value())
                        .role(domain.getRole().name())
                        .createdAt(domain.getCreatedAt())
                        .build(),
                UserEntity.class,
                User.class);
    }
}
*/