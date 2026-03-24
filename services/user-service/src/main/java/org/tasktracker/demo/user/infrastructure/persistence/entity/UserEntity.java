package org.tasktracker.demo.user.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Table(name = "users", schema = "users")
public class UserEntity {
    @Id
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Instant createdAt;
    private boolean active;
}