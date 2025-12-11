package org.example.enrollment;

import java.util.Optional;

import org.example.core.EntityId;

public interface EnrollmentRepository {
    Iterable<Enrollment> findAll();
    
    Optional<Enrollment> findbyId(EntityId id);

    void insert(Enrollment enrollment);

    void update(Enrollment enrollment);

    void delete(EntityId id);
    
}
