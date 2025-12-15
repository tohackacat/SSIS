package org.example.student;

import org.example.core.EntityId;

import java.util.Optional;

public interface StudentRepository {
    Iterable<Student> findAll();

    Optional<Student> findById(EntityId id);

    void insert(Student student);

    void update(Student student);

    void delete(EntityId id);
}

