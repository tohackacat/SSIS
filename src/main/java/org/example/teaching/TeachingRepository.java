package org.example.teaching;
import java.util.Optional;

import org.example.core.EntityId;

public interface TeachingRepository {
    Iterable<Teaching> findAll();

    Optional<Teaching> findById(EntityId id);

    void insert(Teaching teaching);

    void update(Teaching teaching);

    void delete(EntityId id);
}
