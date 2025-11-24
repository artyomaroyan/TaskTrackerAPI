package org.tasktracker.demo.userservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.AuthenticationRequest;
import org.tasktracker.demo.userservice.security.JwtService;
import org.tasktracker.demo.userservice.security.UsernamePasswordAuthenticationManager;
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
    private final JwtService jwtService;
    private final UsernamePasswordAuthenticationManager authenticationManager;

    @Override
    public Mono<String> login(AuthenticationRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()
                ))
                .map(authentication -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    return jwtService.generateAccessToken(userDetails.getUsername());
                });
    }
}