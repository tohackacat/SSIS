package org.example.teaching;

import org.example.core.EntityId;

import java.util.Optional;

public interface TeachingRepository {
    Iterable<Teaching> findAll();

    Optional<Teaching> findById(EntityId id);

    void insert(Teaching teaching);

    void update(Teaching teaching);

    void delete(EntityId id);
}
