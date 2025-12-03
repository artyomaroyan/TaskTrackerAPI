package org.tasktracker.demo.userservice.application.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.tasktracker.demo.userservice.application.ports.in.UserService;
import org.tasktracker.demo.userservice.domain.model.User;
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
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN') or @reactiveSecurityService.isSelfOrAdmin(id)")
    public Mono<User> findUserById(UUID id) {
        log.debug("finding user by ID: {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")));
    }

    @Override
    @PreAuthorize("hasRole('USER') or #username = authentication.principal.username")
    public Mono<User> findUserByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with username " + username + " not found")));
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN') or hasAuthority('DELETE')")
    public Mono<Void> deleteUserById(UUID id) {
        log.debug("Deleting user by ID: {}", id);
        return this.findUserById(id)
                .then(userRepository.delete(id))
                .doOnSuccess(_ -> log.info("Successfully deleted user with ID: {}", id));
    }
}