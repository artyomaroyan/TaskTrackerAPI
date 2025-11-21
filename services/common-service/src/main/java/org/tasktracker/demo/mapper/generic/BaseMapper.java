package org.tasktracker.demo.mapper.generic;

import org.tasktracker.demo.exception.DataMappingException;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

/**
 * Author: Artyom Aroyan
 * Date: 22.11.25
 * Time: 00:18:54
 */
public abstract class BaseMapper<D, E> implements GenericMapper<D, E> {
    private static final Logger log = Logger.getLogger(BaseMapper.class.getName());

    @Override
    public final Mono<D> toDomain(E entity) {
        if (entity == null) {
            log.log(WARNING, String.format("Attempted to map null entity to domain (%s)", getClass().getSimpleName()));
            return Mono.empty();
        }
        try {
            D domain = mapToDomain(entity);
            return domain != null ? Mono.just(domain) : Mono.empty();
        } catch (Exception ex) {
            log.log(SEVERE, String.format("Error mapping entity to domain (%s), %s", getClass().getSimpleName(), ex));
            return Mono.error(new DataMappingException("Mapping failed", ex));
        }
    }

    @Override
    public final Mono<E> toEntity(D domain) {
        if (domain == null) {
            log.log(WARNING, String.format("Attempted to map null domain to entity (%s)", getClass().getSimpleName()));
            return Mono.empty();
        }
        try {
            E entity = mapToEntity(domain);
            return entity != null ? Mono.just(entity) : Mono.empty();
        } catch (Exception ex) {
            log.log(SEVERE, String.format("Error mapping domain to entity (%s), %s", getClass().getSimpleName(), ex));
            return Mono.error(new DataMappingException("Mapping failed", ex));
        }
    }

    protected abstract D mapToDomain(E entity);
    protected abstract E mapToEntity(D domain);
}