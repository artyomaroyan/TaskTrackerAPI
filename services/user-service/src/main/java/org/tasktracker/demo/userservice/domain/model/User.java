package org.tasktracker.demo.userservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
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
    private final String password;
    private final Email email;
    private final Role role;
    private final Instant createdAt;
    private final boolean active;

    public static User create(String username, String password, Email email, Role role, boolean active) {
        return new User(null, username, password, email, role, Instant.now(), active);
    }

    public static User of(UUID id, String username, String password, Email email, Role role, Instant createdAt, boolean active) {
        return new User(id, username, password, email, role, createdAt, active);
    }

    public User changeEmail(Email newEmail) {
        return new User(this.id, this.username, this.password, newEmail, this.role, this.createdAt, this.isActive());
    }

    public User changeRole(Role newRole) {
        return new User(this.id, this.username, this.password, this.email, newRole, this.createdAt, this.isActive());
    }

    public Set<String> getAuthorities() {
        return this.role.getAuthoritiesAsString();
    }

    public boolean hasAuthority(Authorities authorities) {
        return this.role.hasAuthority(authorities);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User that)) return false;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.role, that.role) &&
                Objects.equals(this.createdAt, that.createdAt) &&
                Objects.equals(this.active, that.active);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, username, email, role, createdAt, active);
    }
}