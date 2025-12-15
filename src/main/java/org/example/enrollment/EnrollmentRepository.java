package org.example.enrollment;

import org.example.core.EntityId;

import java.util.Optional;

public interface EnrollmentRepository {
    Iterable<Enrollment> findAll();

    Optional<Enrollment> findById(EntityId id);

    void insert(Enrollment enrollment);

    void update(Enrollment enrollment);

    void delete(EntityId id);
}
