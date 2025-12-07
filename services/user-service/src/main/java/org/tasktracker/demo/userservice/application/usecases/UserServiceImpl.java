package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.dto.UserResponse;
import org.tasktracker.demo.userservice.application.mapper.UserMapper;
import org.tasktracker.demo.userservice.application.ports.in.UserService;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.domain.exception.UserNotFoundException;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:52:24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN') or @reactiveSecurityService.isSelfOrAdmin(#id)")
    public Mono<UserResponse> findUserById(UUID id) {
        log.debug("finding user by ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")));
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasAuthority('READ')")
    public Mono<UserResponse> findUserByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with username " + username + " not found")));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN') or hasAuthority('DELETE')")
    public Mono<Void> deleteUserById(UUID id) {
        log.debug("Deleting user by ID: {}", id);
        return this.findUserById(id)
                .then(userRepository.delete(id))
                .doOnSuccess(_ -> log.info("Successfully deleted user with ID: {}", id));
    }
}