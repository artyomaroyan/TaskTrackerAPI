package org.tasktracker.demo.mapper.generic;

import reactor.core.publisher.Mono;

/**
 * Author: Artyom Aroyan
 * Date: 22.11.25
 * Time: 00:15:31
 */
public interface GenericMapper<D, E> {
    Mono<D> toDomain(E entity);
    Mono<E> toEntity(D domain);
}