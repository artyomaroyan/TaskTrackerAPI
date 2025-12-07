package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.tasktracker.demo.userservice.infrastructure.configuration.PublicEndpoints;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ServerWebExchangeMatcher csrfMatcher;
    private final CorsConfigurationSource configurationSource;
    private final ReactiveAuthenticationManager authenticationManager;
    private final MultipleAuthenticationConverter authenticationConverter;

    @Bean
    protected SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfMatcher))
                .cors(cors -> cors.configurationSource(configurationSource))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, _) ->
                                Mono.fromRunnable(() -> {
                                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                }))
                        .accessDeniedHandler((exchange, _) ->
                                Mono.fromRunnable(() -> {
                                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                }))
                )
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PublicEndpoints.ALL)
                            .permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/user/get/id/**")
                            .authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/v1/user/get/username/**")
                            .authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**")
                            .authenticated()
                        .anyExchange()
                            .authenticated()
                )
                .authenticationManager(authenticationManager)
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(authenticationManager);
        webFilter.setServerAuthenticationConverter(authenticationConverter);
        webFilter.setRequiresAuthenticationMatcher(requiresAuthenticationMatcher());
        return webFilter;
    }

    private ServerWebExchangeMatcher requiresAuthenticationMatcher() {
        List<ServerWebExchangeMatcher> publicMatchers = Arrays.stream(PublicEndpoints.ALL)
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .collect(Collectors.toUnmodifiableList());

        OrServerWebExchangeMatcher publicOrMatcher = new OrServerWebExchangeMatcher(publicMatchers);

        return exchange -> publicOrMatcher.matches(exchange)
                .flatMap(result -> result.isMatch()
                        ? ServerWebExchangeMatcher.MatchResult.notMatch()
                        : ServerWebExchangeMatcher.MatchResult.match());
    }
}