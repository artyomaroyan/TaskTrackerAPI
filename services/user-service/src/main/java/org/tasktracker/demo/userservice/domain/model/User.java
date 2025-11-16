package org.tasktracker.demo.userservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 12:10:38
 */
@Getter
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class User {
    private final UUID id;
    private final String username;
    private final Email email;
    private final Role role;
    private final Instant createdAt;

    public static User create(String name, Email email, Role role) {
        return new User(UUID.randomUUID(), name, email, role, Instant.now());
    }

    public static User of(UUID id, String name, Email email, Role role, Instant createdAt) {
        return new User(id, name, email, role, createdAt);
    }

    public User changeEmail(Email newEmail) {
        return new User(this.id, this.username, newEmail, this.role, this.createdAt);
    }

    public User changeRole(Role newRole) {
        return new User(this.id, this.username, this.email, newRole, this.createdAt);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User that)) return false;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.role, that.role) &&
                Objects.equals(this.createdAt, that.createdAt);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, username, email, role, createdAt);
    }
}