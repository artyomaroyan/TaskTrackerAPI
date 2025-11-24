package org.tasktracker.demo.userservice.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 17:58:14
 */
public class BearerToken extends AbstractAuthenticationToken {
    private final String token;

    public BearerToken(String token) {
        super(NO_AUTHORITIES);
        this.token = token;
    }

    @Override
    public String getCredentials() {
        return this.token;
    }

    @Override
    public String getPrincipal() {
        return this.token;
    }
}