package org.tasktracker.demo.task.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.tasktracker.demo.configuration.PublicEndpoints;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 17.01.26
 * Time: 22:56:13
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
class SecurityConfiguration {

    @Bean
    protected SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.SWAGGER)
                            .permitAll()
                        .pathMatchers("/api/v1/task/auth/actuator")
                            .permitAll()
//                        .pathMatchers("/api/v1/task/auth/create")
//                            .hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                        .pathMatchers("/api/v1/task/auth/create")
                            .hasAuthority("CREATE")
                        .anyExchange()
                            .authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        ))
                .build();
    }

    @Bean
    protected ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null && !roles.isEmpty()) {
                authorities.addAll(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList());
            }

            List<String> authoritiesList = jwt.getClaimAsStringList("authorities");
            if (authoritiesList != null && !authorities.isEmpty()) {
                authorities.addAll(authoritiesList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());
            }
            return Flux.fromIterable(authorities);
        });
        return converter;
    }
}