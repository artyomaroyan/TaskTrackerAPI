package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.tasktracker.demo.userservice.application.ports.out.KeyProvider;
import org.tasktracker.demo.configuration.PublicEndpoints;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPublicKey;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 19:31:52
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
                .httpBasic(Customizer.withDefaults())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.ALL)
                            .permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/user/get/id/**")
                            .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/user/get/username/**")
                            .hasAuthority("READ")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**")
                            .hasAuthority("DELETE")
                        .anyExchange()
                            .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    @Bean
    protected ReactiveJwtDecoder jwtDecoder(KeyProvider keyProvider) {
        return NimbusReactiveJwtDecoder.withPublicKey((RSAPublicKey) keyProvider.getPublicKey()).build();
    }

    @Bean
    protected Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");

        return jwt -> Mono.just(new JwtAuthenticationToken(
                jwt,
                authoritiesConverter.convert(jwt),
                jwt.getSubject()
        ));
    }
}