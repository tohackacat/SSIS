package org.example.teaching.service;

import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.faculty.Faculty;
import org.example.faculty.FacultyRepository;
import org.example.person.Person;
import org.example.person.PersonRepository;
import org.example.core.EntityId;
import org.example.teaching.Teaching;
import org.example.teaching.TeachingRepository;
import org.example.teaching.TeachingService;
import org.example.teaching.dto.TeachingDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultTeachingService implements TeachingService {
    private final TeachingRepository teachingRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final PersonRepository personRepository;

    public DefaultTeachingService(
            TeachingRepository teachingRepository,
            CourseRepository courseRepository,
            FacultyRepository facultyRepository,
            PersonRepository personRepository
    ) {
        this.teachingRepository = teachingRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<TeachingDto> findAll() {
        List<TeachingDto> result = new ArrayList<>();
        for (Teaching teaching : teachingRepository.findAll()) {
            Optional<TeachingDto> dto = toDto(teaching);
            dto.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Optional<TeachingDto> findById(String id) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Teaching> teaching = teachingRepository.findById(entityId);
        if (teaching.isEmpty()) {
            return Optional.empty();
        }
        return toDto(teaching.get());
    }

    @Override
    public TeachingDto create(String courseId, String facultyId, String role) {
        EntityId courseEntityId = EntityId.fromString(courseId);
        EntityId facultyEntityId = EntityId.fromString(facultyId);

        Optional<Course> courseOpt = courseRepository.findById(courseEntityId);
        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }
        Optional<Faculty> facultyOpt = facultyRepository.findById(facultyEntityId);
        if (facultyOpt.isEmpty()) {
            throw new IllegalArgumentException("Faculty not found: " + facultyId);
        }

        EntityId teachingId = EntityId.random();
        Teaching teaching = new Teaching(teachingId, courseEntityId, facultyEntityId, role);
        teachingRepository.insert(teaching);

        return toDto(teaching).orElseThrow();
    }

    @Override
    public TeachingDto update(String id, String role) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Teaching> teachingOpt = teachingRepository.findById(entityId);
        if (teachingOpt.isEmpty()) {
            throw new IllegalArgumentException("Teaching not found: " + id);
        }
        Teaching existing = teachingOpt.get();
        Teaching updated = new Teaching(existing.id(), existing.courseId(), existing.facultyId(), role);
        teachingRepository.update(updated);
        return toDto(updated).orElseThrow();
    }

    @Override
    public void delete(String id) {
        EntityId entityId = EntityId.fromString(id);
        teachingRepository.delete(entityId);
    }

    private Optional<TeachingDto> toDto(Teaching teaching) {
        Optional<Course> courseOpt = courseRepository.findById(teaching.courseId());
        if (courseOpt.isEmpty()) {
            return Optional.empty();
        }
        Course course = courseOpt.get();

        Optional<Faculty> facultyOpt = facultyRepository.findById(teaching.facultyId());
        if (facultyOpt.isEmpty()) {
            return Optional.empty();
        }
        Faculty faculty = facultyOpt.get();

        Optional<Person> personOpt = personRepository.findById(faculty.personId());
        if (personOpt.isEmpty()) {
            return Optional.empty();
        }
        Person person = personOpt.get();

        return Optional.of(new TeachingDto(
                teaching.id().toString(),
                course.id().toString(),
                course.code(),
                course.name(),
                faculty.id().toString(),
                person.fullName(),
                teaching.role()
        ));
    }
}
