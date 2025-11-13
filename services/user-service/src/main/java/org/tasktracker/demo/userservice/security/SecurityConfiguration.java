package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 19:31:52
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final ServerWebExchangeMatcher csrfMatcher;
    private final CorsConfigurationSource configurationSource;

    @Bean
    protected SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfMatcher))
                .cors(cors -> cors.configurationSource(configurationSource))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.ALL)
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authenticationManager(_ -> Mono.error(new IllegalStateException("JWT authentication should handle this")))
                .addFilterBefore(this.jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}