package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import org.tasktracker.demo.userservice.application.ports.in.AuthenticationService;
import org.tasktracker.demo.userservice.security.UserIdentity;
import org.tasktracker.demo.userservice.security.BasicAuthenticationManager;
import org.tasktracker.demo.userservice.application.ports.out.TokenProvider;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 23.11.25
 * Time: 23:51:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokenProvider tokenProvider;
    private final BasicAuthenticationManager authenticationManager;

    @Override
    public Mono<String> login(AuthenticationRequest request) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()
                ))
                .flatMap(authentication -> {
                    UserIdentity userIdentity = (UserIdentity) authentication.getPrincipal();
                    return tokenProvider.generateAccessToken(userIdentity);
                });
    }
}