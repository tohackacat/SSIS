package org.example.person;
import java.util.Optional;

import org.example.core.EntityId;

public interface PersonRepository {
    Iterable<Person> findAll();
    Optional<Person> findById(EntityId id);

    void insert(Person person);
    void update(Person person);
    void delete(EntityId id);

    
}
