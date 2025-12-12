package org.example.person;

import java.util.List;
import java.util.Optional;

import org.example.person.dto.PersonDto;

public interface PersonService {
    List<PersonDto> findAll();

    Optional<PersonDto> findById(String id);

    PersonDto create(String fullName, String email);

    PersonDto update(String id, String fullName, String email);

    void delete(String id);
}

