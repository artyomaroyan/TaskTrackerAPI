package org.tasktracker.demo.userservice.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.exception.DataMappingException;
import org.tasktracker.demo.mapper.generic.BaseMapper;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.infrastructure.persistence.entity.UserEntity;

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
                    entity.getCreatedAt());
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
                .createdAt(domain.getCreatedAt())
                .build();
    }

//    public User toDomain(UserEntity entity) {
//        if (entity == null) {
//            log.warn("Attempted to map null UserEntity to domain");
//            return null;
//        }
//
//        try {
//            return User.of(
//                    entity.getId(),
//                    entity.getUsername(),
//                    entity.getPassword(),
//                    new Email(entity.getEmail()),
//                    Role.valueOf(entity.getRole()),
//                    entity.getCreatedAt());
//        } catch (IllegalArgumentException ex) {
//            log.error("Failed to map UserEntity to domain due to invalid role: {}", entity.getRole(), ex);
//            throw new DataMappingException("Invalid role value " + entity.getRole(), ex);
//        }
//    }
//
//    public UserEntity toEntity(User user) {
//        if (user == null) {
//            log.warn("Attempted to map null User to entity");
//            return null;
//        }
//
//        return UserEntity.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .email(user.getEmail().value())
//                .role(user.getRole().name())
//                .createdAt(user.getCreatedAt())
//                .build();
//    }
}