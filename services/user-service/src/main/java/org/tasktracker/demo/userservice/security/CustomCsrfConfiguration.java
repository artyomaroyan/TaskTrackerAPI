package org.tasktracker.demo.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Artyom Aroyan
 * Date: 13.11.25
 * Time: 21:36:25
 */
@Configuration
class CustomCsrfConfiguration {
    @Bean
    protected ServerWebExchangeMatcher csrfMatcher() {
        List<String> ignore = Stream.of(
                        PublicEndpoints.SWAGGER,
                        PublicEndpoints.JWKS
                )
                .flatMap(Stream::of)
                .toList();

        List<ServerWebExchangeMatcher> matchers = ignore
                .stream()
                .map(PathPatternParserServerWebExchangeMatcher::new)
                .collect(Collectors.toUnmodifiableList());

        OrServerWebExchangeMatcher matcher = new OrServerWebExchangeMatcher(matchers);

        return exchange -> matcher.matches(exchange)
                .flatMap(result -> result.isMatch() ?
                        ServerWebExchangeMatcher.MatchResult.notMatch() :
                        ServerWebExchangeMatcher.MatchResult.match());
    }
}