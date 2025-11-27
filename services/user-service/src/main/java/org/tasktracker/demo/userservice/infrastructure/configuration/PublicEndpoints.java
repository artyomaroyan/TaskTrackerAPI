package org.tasktracker.demo.userservice.infrastructure.configuration;

import java.util.stream.Stream;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 21:37:15
 */
public final class PublicEndpoints {

    static final String[] SWAGGER = {
            "/webjars/**", "/webjars/swagger-ui/**", "/v2/api-docs", "/v3/api-docs/", "/v3/api-docs/**",
            "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security"
    };

    static final String[] JWKS = {
            "/.well-known/jwks.json"
    };

    static final String[] WHITELIST = {
            "/api/v1/user/register",
            "/api/v1/user/login"
    };

    static final String[] ALL = Stream.of(SWAGGER, JWKS, WHITELIST)
            .flatMap(Stream::of)
            .toArray(String[]::new);
}