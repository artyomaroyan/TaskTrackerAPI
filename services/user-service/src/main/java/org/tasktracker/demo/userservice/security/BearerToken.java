package org.tasktracker.demo.userservice.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 17:58:14
 */
public class BearerToken extends AbstractAuthenticationToken {
    @Getter
    private final String token;

    public BearerToken(String token) {
        super(Collections.emptyList());
        this.token = token;
        setAuthenticated(false);
    }

    public BearerToken(String token, Objects principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        setAuthenticated(true);
        setDetails(principal);
    }

    @Override
    public String getCredentials() {
        return this.token;
    }

    @Override
    public String getPrincipal() {
        return this.token;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}