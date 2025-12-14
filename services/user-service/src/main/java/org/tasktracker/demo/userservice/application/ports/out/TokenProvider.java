package org.tasktracker.demo.userservice.application.ports.out;

import org.tasktracker.demo.userservice.domain.model.UserIdentity;
import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 24.11.25
 * Time: 20:28:29
 */
public interface TokenProvider {
    Mono<String> generateAccessToken(UserIdentity user);
}