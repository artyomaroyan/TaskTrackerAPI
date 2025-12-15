package org.tasktracker.demo.userservice.domain.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        roles.forEach(role -> grantedAuthorities
                .add(new SimpleGrantedAuthority("ROLE_" + role.name())));

        authorities.forEach(authority -> grantedAuthorities
                .add(new SimpleGrantedAuthority(authority)));

        return Collections.unmodifiableSet(grantedAuthorities);
    }

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//
//        if (roles != null && !roles.isEmpty()) {
//            String[] roleArray = roles.split(",");
//            for (String role : roleArray) {
//                role = role.trim();
//                if (!role.isEmpty()) {
//                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                }
//            }
//        }
//
//        grantedAuthorities.addAll(authorities.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toUnmodifiableSet()));
//
//        return grantedAuthorities;
//    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public UserIdentity withoutPassword() {
        return new UserIdentity(this.username, null, this.roles, this.authorities, this.enabled);
    }
}