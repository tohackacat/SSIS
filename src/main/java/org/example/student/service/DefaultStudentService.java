package org.example.student.service;

import org.example.core.EntityId;
import org.example.person.Person;
import org.example.person.PersonRepository;
import org.example.student.Student;
import org.example.student.StudentRepository;
import org.example.student.StudentService;
import org.example.student.dto.StudentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultStudentService implements StudentService {
    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;

    public DefaultStudentService(StudentRepository studentRepository, PersonRepository personRepository) {
        this.studentRepository = studentRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<StudentDto> findAll() {
        List<StudentDto> result = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            Optional<StudentDto> dto = toDto(student);
            dto.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Optional<StudentDto> findById(String id) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Student> student = studentRepository.findById(entityId);
        if (student.isEmpty()) {
            return Optional.empty();
        }
        return toDto(student.get());
    }

    @Override
    public StudentDto create(String fullName, String email, int enrollmentYear) {
        EntityId personId = EntityId.random();
        EntityId studentId = EntityId.random();

        Person person = new Person(personId, fullName, email == null || email.isBlank()
                ? java.util.Optional.empty()
                : java.util.Optional.of(email));
        personRepository.insert(person);

        Student student = new Student(studentId, personId, enrollmentYear);
        studentRepository.insert(student);

        return new StudentDto(studentId.toString(), fullName, email, enrollmentYear);
    }

    @Override
    public StudentDto update(String id, String fullName, String email, int enrollmentYear) {
        EntityId studentId = EntityId.fromString(id);
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found: " + id);
        }
        Student student = studentOpt.get();
        Optional<Person> personOpt = personRepository.findById(student.personId());
        if (personOpt.isEmpty()) {
            throw new IllegalStateException("Person not found for student " + id);
        }
        Person existingPerson = personOpt.get();
        Person updatedPerson = new Person(
                existingPerson.id(),
                fullName,
                email == null || email.isBlank()
                        ? java.util.Optional.empty()
                        : java.util.Optional.of(email)
        );
        personRepository.update(updatedPerson);

        Student updatedStudent = new Student(student.id(), student.personId(), enrollmentYear);
        studentRepository.update(updatedStudent);

        return new StudentDto(id, fullName, email, enrollmentYear);
    }

    @Override
    public void delete(String id) {
        EntityId studentId = EntityId.fromString(id);
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            return;
        }
        Student student = studentOpt.get();
        studentRepository.delete(student.id());
        personRepository.delete(student.personId());
    }

    private Optional<StudentDto> toDto(Student student) {
        Optional<Person> personOpt = personRepository.findById(student.personId());
        if (personOpt.isEmpty()) {
            return Optional.empty();
        }
        Person person = personOpt.get();
        String email = person.email().orElse("");
        return Optional.of(new StudentDto(
                student.id().toString(),
                person.fullName(),
                email,
                student.enrollmentYear()
        ));
    }
}

