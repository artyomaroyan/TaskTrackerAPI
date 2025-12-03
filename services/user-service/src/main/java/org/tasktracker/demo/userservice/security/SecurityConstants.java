package org.tasktracker.demo.userservice.security;

import org.tasktracker.demo.userservice.domain.model.Authorities;
import org.tasktracker.demo.userservice.domain.model.Role;

/**
 * Author: Artyom Aroyan
 * Date: 01.12.25
 * Time: 01:30:48
 */
final class SecurityConstants {
    static final String ROLE_USER = Role.USER.name().toUpperCase();
    static final String ROLE_ADMIN = Role.ADMIN.name().toUpperCase();
    static final String AUTHORITY_CREATE = Authorities.CREATE.name().toUpperCase();
    static final String AUTHORITY_READ = Authorities.READ.name().toUpperCase();
    static final String AUTHORITY_UPDATE = Authorities.UPDATE.name().toUpperCase();
    static final String AUTHORITY_DELETE = Authorities.DELETE.name().toUpperCase();
}