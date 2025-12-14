package org.tasktracker.demo.userservice.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.tasktracker.demo.userservice.domain.model.Role.USER;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 12:10:38
 */
@Builder(access = PRIVATE)
public record User(UUID id, String username, String password, Email email,
                   Role role, Instant createdAt, boolean active) {

    public User(UUID id, String username, String password, Email email, Role role, Instant createdAt, boolean active) {
        this.id = id;
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.password = Objects.requireNonNull(password, "Password cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.role = Objects.requireNonNull(role, "Role cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.active = active;
    }

    public static User create(String username, String password, Email email) {
        return new User(null, username, password, email, USER, Instant.now(), true);
    }

    public static User of(UUID id, String username, String password, Email email, Role role, Instant createdAt, boolean active) {
        return new User(id, username, password, email, role, createdAt, active);
    }

    public User changeEmail(Email newEmail) {
        return new User(this.id, this.username, this.password, newEmail, this.role, this.createdAt, this.active());
    }

    public User changeRole(Role newRole) {
        return new User(this.id, this.username, this.password, this.email, newRole, this.createdAt, this.active());
    }

    public Set<String> getAuthorities() {
        return role.getAuthoritiesAsString();
    }
}