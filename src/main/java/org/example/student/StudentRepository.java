package org.example.student;

import java.util.Optional;

import org.example.core.EntityId;

public interface  StudentRepository {
     Iterable<Student> findAll();

    Optional<Student> findById(EntityId id);

    void insert(Student student);

    void update(Student student);

    void delete(EntityId id);
}

