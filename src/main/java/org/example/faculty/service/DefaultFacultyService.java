package org.example.faculty.service;

import org.example.core.EntityId;
import org.example.faculty.Faculty;
import org.example.faculty.FacultyRepository;
import org.example.faculty.FacultyService;
import org.example.faculty.dto.FacultyDto;
import org.example.person.Person;
import org.example.person.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultFacultyService implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final PersonRepository personRepository;

    public DefaultFacultyService(FacultyRepository facultyRepository, PersonRepository personRepository) {
        this.facultyRepository = facultyRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<FacultyDto> findAll() {
        List<FacultyDto> result = new ArrayList<>();
        for (Faculty faculty : facultyRepository.findAll()) {
            Optional<FacultyDto> facultydto = toDto(faculty);
            facultydto.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Optional<FacultyDto> findById(String id) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Faculty> faculty = facultyRepository.findById(entityId);
        if (faculty.isEmpty()) {
            return Optional.empty();
        }
        return toDto(faculty.get());
    }

    @Override
    public FacultyDto create(String fullName, String email, String department) {
        EntityId personId = EntityId.random();
        EntityId facultyId = EntityId.random();

        Optional<String> optEmail;
        if (email == null || email.isBlank()) {
            optEmail = Optional.empty();
        } else {
            optEmail = Optional.of(email);
        }

        Person person = new Person(personId, fullName, optEmail);
        personRepository.insert(person);

        Faculty faculty = new Faculty(facultyId, personId, department);
        facultyRepository.insert(faculty);

        return new FacultyDto(facultyId.toString(), fullName, email, department);
    }

    @Override
    public FacultyDto update(String id, String fullName, String email, String department) {
        EntityId facultyId = EntityId.fromString(id);
        Optional<Faculty> optFaculty = facultyRepository.findById(facultyId);
        if (optFaculty.isEmpty()) {
            throw new IllegalArgumentException("Faculty " + id + " not found");
        }
        Faculty faculty = optFaculty.get();

        Optional<Person> optPerson = personRepository.findById(faculty.personId());
        if (optPerson.isEmpty()) {
            throw new IllegalArgumentException("Person " + id + " not found");
        }

        Optional<String> optEmail;
        if (email == null || email.isBlank()) {
            optEmail = Optional.empty();
        } else {
            optEmail = Optional.of(email);
        }

        Person existingPerson = optPerson.get();
        Person updatedPerson = new Person(
                existingPerson.id(),
                fullName,
                optEmail
        );
        personRepository.update(updatedPerson);

        Faculty updatedFaculty = new Faculty(faculty.id(), faculty.personId(), department);
        facultyRepository.update(updatedFaculty);

        return new FacultyDto(id, fullName, email, department);
    }

    @Override
    public void delete(String id) {
        EntityId facultyId = EntityId.fromString(id);
        Optional<Faculty> optFaculty = facultyRepository.findById(facultyId);
        if (optFaculty.isEmpty()) {
            return;
        }
        Faculty faculty = optFaculty.get();
        facultyRepository.delete(faculty.id());
        personRepository.delete(faculty.personId());
    }

    private Optional<FacultyDto> toDto(Faculty faculty) {
        Optional<Person> optPerson = personRepository.findById(faculty.personId());
        if (optPerson.isEmpty()) {
            return Optional.empty();
        }
        Person person = optPerson.get();
        String email = person.email().orElse("");

        return Optional.of(new FacultyDto(
                faculty.id().toString(),
                person.fullName(),
                email,
                faculty.department()
        ));
    }
}
