package org.example.person;

import org.example.core.EntityId;

import java.util.Optional;

public interface PersonRepository {
    Iterable<Person> findAll();

    Optional<Person> findById(EntityId id);

    void insert(Person person);

    void update(Person person);

    void delete(EntityId id);


}
