package org.tasktracker.demo.userservice.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.exception.DataMappingException;
import org.tasktracker.demo.mapper.BaseMapper;
import org.tasktracker.demo.userservice.application.dto.UserResponse;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 18:14:19
 */
@Slf4j
@Component
public class UserMapper extends BaseMapper<User, UserEntity> {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.id(),
                user.username(),
                user.email().value(),
                user.role().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet()),
                user.createdAt(),
                user.active()
        );
    }

    @Override
    protected User mapToDomain(UserEntity entity) {
        try {
            Set<Role> roles = entity.getRole().stream()
                    .map(s -> s.replaceAll("[{}]", ""))
                    .map(Role::valueOf)
                    .collect(Collectors.toUnmodifiableSet());

            return User.of(
                    entity.getId(),
                    entity.getUsername(),
                    entity.getPassword(),
                    new Email(entity.getEmail()),
                    roles,
                    entity.getCreatedAt(),
                    entity.isActive());
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid role value: {}", entity.getRole(), ex);
            throw new DataMappingException("Invalid role value " + entity.getRole(), ex.getCause());
        }
    }

    @Override
    protected UserEntity mapToEntity(User domain) {
        return UserEntity.builder()
                .id(domain.id())
                .username(domain.username())
                .password(domain.password())
                .email(domain.email().value())
                .role(domain.role().stream()
                        .map(Role::name)
                        .collect(Collectors.toSet()))
                .createdAt(domain.createdAt())
                .active(domain.active())
                .build();
    }
}