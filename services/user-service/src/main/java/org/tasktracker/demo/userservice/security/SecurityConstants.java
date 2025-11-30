package org.tasktracker.demo.userservice.security;

import org.tasktracker.demo.userservice.domain.model.Authorities;
import org.tasktracker.demo.userservice.domain.model.Role;

/**
 * Author: Artyom Aroyan
 * Date: 01.12.25
 * Time: 01:30:48
 */
final class SecurityConstants {
    private static final String ROLE_USER = Role.USER.name().toUpperCase();
    private static final String ROLE_ADMIN = Role.ADMIN.name().toUpperCase();
    private static final String AUTHORITY_READ = Authorities.READ.name().toUpperCase();
    private static final String AUTHORITY_WRITE = Authorities.CREATE.name().toUpperCase();
    private static final String AUTHORITY_DELETE = Authorities.DELETE.name().toUpperCase();
}