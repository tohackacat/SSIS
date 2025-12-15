package org.example.faculty;

import org.example.core.EntityId;

import java.util.Optional;

public interface FacultyRepository {

    Iterable<Faculty> findAll();

    Optional<Faculty> findById(EntityId id);

    void insert(Faculty faculty);

    void update(Faculty faculty);

    void delete(EntityId id);

}
