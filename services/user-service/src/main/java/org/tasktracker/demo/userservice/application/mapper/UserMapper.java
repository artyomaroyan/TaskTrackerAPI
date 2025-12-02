package org.tasktracker.demo.userservice.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.exception.DataMappingException;
import org.tasktracker.demo.mapper.BaseMapper;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;

import java.util.Set;

import static org.tasktracker.demo.userservice.domain.model.Authorities.*;

/**
 * Author: Artyom Aroyan
 * Date: 21.11.25
 * Time: 18:14:19
 */
@Slf4j
@Component
public class UserMapper extends BaseMapper<User, UserEntity> {

    @Override
    protected User mapToDomain(UserEntity entity) {
        try {
            return User.of(
                    entity.getId(),
                    entity.getUsername(),
                    entity.getPassword(),
                    new Email(entity.getEmail()),
                    Role.valueOf(entity.getRole()),
                    Set.of(CREATE.name(), READ.name(), UPDATE.name(), DELETE.name()),
                    entity.getCreatedAt(),
                    entity.isActive());
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid role value: {}", entity.getRole(), ex);
            throw new DataMappingException("Invalid role value" + entity.getRole(), ex.getCause());
        }
    }

    @Override
    protected UserEntity mapToEntity(User domain) {
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .email(domain.getEmail().value())
                .role(domain.getRole().name())
                .authorities(domain.getAuthorities())
                .createdAt(domain.getCreatedAt())
                .active(domain.isActive())
                .build();
    }
}