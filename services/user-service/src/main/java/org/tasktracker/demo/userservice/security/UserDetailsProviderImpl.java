package org.tasktracker.demo.userservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.application.ports.out.UserDetailsProvider;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 25.11.25
 * Time: 14:36:12
 */
@Component
@RequiredArgsConstructor
public class UserDetailsProviderImpl implements UserDetailsProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserIdentity(
                        user.getUsername(),
                        user.getAuthorities(),
                        user.isActive()
                ));
    }

    @Override
    public Mono<Boolean> validCredentials(String username, String password) {
        return findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .defaultIfEmpty(false);
    }
}