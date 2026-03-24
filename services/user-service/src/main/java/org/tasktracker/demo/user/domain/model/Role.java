package org.tasktracker.demo.user.domain.model;

import java.util.Set;
import java.util.stream.Collectors;

import static org.tasktracker.demo.user.domain.model.Authorities.*;

/**
 * Author: Artyom Aroyan
 * Date: 01.11.25
 * Time: 12:13:31
 */
public enum Role {
    USER(Set.of(CREATE_SELF, READ, UPDATE_SELF)),
    ADMIN(Set.of(CREATE, CREATE_SELF, READ, UPDATE, UPDATE_SELF, DELETE, MANAGE_USERS));

    private final Set<Authorities> grantedAuthorities;

    Role(Set<Authorities> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public Set<String> getAuthoritiesAsString() {
        return grantedAuthorities.stream()
                .map(Authorities::name)
                .collect(Collectors.toSet());
    }
}