package org.tasktracker.demo.user.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.exception.DataMappingException;
import org.tasktracker.demo.mapper.BaseMapper;
import org.tasktracker.demo.user.application.dto.UserResponse;
import org.tasktracker.demo.user.domain.model.Email;
import org.tasktracker.demo.user.domain.model.Role;
import org.tasktracker.demo.user.domain.model.User;
import org.tasktracker.demo.user.infrastructure.persistence.entity.UserEntity;

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
                user.role().name(),
                user.createdAt(),
                user.active()
        );
    }

    @Override
    protected User mapToDomain(UserEntity entity) {
        try {
            return User.of(
                    entity.getId(),
                    entity.getUsername(),
                    entity.getPassword(),
                    new Email(entity.getEmail()),
                    Role.valueOf(entity.getRole()),
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
                .role(domain.role().name())
                .createdAt(domain.createdAt())
                .active(domain.active())
                .build();
    }
}