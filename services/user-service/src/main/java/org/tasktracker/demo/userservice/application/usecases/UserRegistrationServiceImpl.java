package org.tasktracker.demo.userservice.application.usecases;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasktracker.demo.userservice.application.dto.UserRequest;
import org.tasktracker.demo.userservice.application.ports.in.UserRegistrationService;
import org.tasktracker.demo.userservice.domain.model.Email;
import org.tasktracker.demo.userservice.domain.model.Role;
import org.tasktracker.demo.userservice.domain.model.User;
import org.tasktracker.demo.userservice.application.ports.out.UserRepository;
import org.tasktracker.demo.userservice.application.exception.DataAccessException;
import org.tasktracker.demo.userservice.application.exception.RegistrationException;
import org.tasktracker.demo.userservice.domain.exception.UserExistenceException;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Pattern;

import static org.tasktracker.demo.userservice.domain.model.Authorities.*;

/**
 * Author: Artyom Aroyan
 * Date: 15.11.25
 * Time: 21:52:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Mono<User> register(@NotNull UserRequest request) {
        return validateRequest(request)
                .flatMap(this::checkUserExistence)
                .flatMap(this::createAndSaveUser)
                .onErrorMap(DataAccessException.class, ex ->
                        new RegistrationException("Registration failed", ex.getCause()));
    }

    private Mono<User> createAndSaveUser(UserRequest request) {
        try {
            User newUser = User.create(
                    request.username(),
                    passwordEncoder.encode(request.password()),
                    new Email(request.email()),
                    Set.of(CREATE.name(), UPDATE.name(), DELETE.name()), // setting users authority logic should be improved!!!.
                    Role.USER,
                    true
            );
            return userRepository.save(newUser);
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