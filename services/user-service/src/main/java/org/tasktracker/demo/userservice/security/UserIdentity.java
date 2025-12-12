package org.tasktracker.demo.userservice.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tasktracker.demo.userservice.domain.model.Role;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:30:11
 */
public record UserIdentity(
        String username,
        String password,
        Set<Role> roles,
        Set<String> authorities,
        boolean enabled) implements UserDetails {

    public UserIdentity(String username, String password, Set<Role> roles, Set<String> authorities, boolean enabled) {
        this.username = Objects.requireNonNull(username);
        this.password = password;
        this.roles = Objects.requireNonNull(roles);
        this.authorities = Objects.requireNonNull(authorities);
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}