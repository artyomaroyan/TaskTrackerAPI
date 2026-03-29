package org.tasktracker.demo.user.application.service;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasktracker.demo.user.application.dto.UserRequest;
import org.tasktracker.demo.user.application.dto.UserResponse;
import org.tasktracker.demo.user.application.exception.DataAccessException;
import org.tasktracker.demo.user.application.exception.RegistrationException;
import org.tasktracker.demo.user.application.mapper.UserMapper;
import org.tasktracker.demo.user.application.ports.in.UserRegistrationService;
import org.tasktracker.demo.user.application.ports.out.UserRepository;
import org.tasktracker.demo.user.domain.exception.UserExistenceException;
import org.tasktracker.demo.user.domain.model.Email;
import org.tasktracker.demo.user.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:52:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @PreAuthorize("permitAll()")
    public Mono<UserResponse> register(@NotNull UserRequest request) {
        return validateRequest(request)
                .flatMap(this::checkUserExistence)
                .flatMap(this::createAndSaveUser)
                .onErrorMap(DataAccessException.class, ex ->
                        new RegistrationException("Registration failed", ex.getCause()));
    }

    private Mono<UserResponse> createAndSaveUser(UserRequest request) {
        try {
            User newUser = User.create(
                    request.username(),
                    passwordEncoder.encode(request.password()),
                    new Email(request.email())
            );
            return userRepository.save(newUser)
                    .map(userMapper::toResponse);
        } catch (RegistrationException ex) {
            return Mono.error(new RegistrationException("Failed to register user", ex.getCause()));
        }
    }

    private Mono<UserRequest> checkUserExistence(UserRequest request) {
        final Email email = new Email(request.email());
        return userRepository.existsByEmail(email)
                .flatMap(exist -> exist ?
                        Mono.error(new UserExistenceException("User with " + email.value() + " already exists!")) :
                        Mono.just(request));
    }

    private Mono<UserRequest> validateRequest(@NotNull UserRequest request) {
        if (!isValidUsername(request.username())) {
            return Mono.error(new ValidationException("Invalid username!"));
        } else if (!isValidPassword(request.password())) {
            return Mono.error(new ValidationException("Invalid password!"));
        }
        return Mono.just(request);
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.,])[A-Za-z\\d@$!%*?&.,]{8,20}$");
        return pattern.matcher(password).matches();
    }

    private boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{5,20}$");
        return pattern.matcher(username).matches();
    }
}