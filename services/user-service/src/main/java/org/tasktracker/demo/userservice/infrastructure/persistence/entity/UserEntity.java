package org.tasktracker.demo.userservice.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 13:21:33
 */
@Getter
@Builder
@Table("users")
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class UserEntity {
    @Id
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Instant createdAt;
}