package org.example.faculty;

import java.util.Optional;

import org.example.core.EntityId;

public interface FacultyRepository {

    Iterable<Faculty> findAll();

    Optional<Faculty> findById(EntityId id);

    void insert(Faculty faculty);

    void update(Faculty faculty);

    void delete(EntityId id);
    
}
