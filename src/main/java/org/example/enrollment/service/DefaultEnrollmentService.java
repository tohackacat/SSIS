package org.example.enrollment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.core.EntityId;
import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.enrollment.Enrollment;
import org.example.enrollment.EnrollmentRepository;
import org.example.enrollment.EnrollmentService;
import org.example.enrollment.dto.EnrollmentDto;
import org.example.person.Person;
import org.example.person.PersonRepository;
import org.example.student.Student;
import org.example.student.StudentRepository;

public class DefaultEnrollmentService implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;
    private final CourseRepository courseRepository;

    public DefaultEnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            PersonRepository personRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<EnrollmentDto> findAll() {
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollmentRepository.findAll()) {
            Optional<EnrollmentDto> dto = toDto(enrollment);
            dto.ifPresent(enrollments::add);
        }
        return enrollments;
    }

    @Override
    public Optional<EnrollmentDto> findById(String id) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Enrollment> enrollment = enrollmentRepository.findById(entityId);
        return enrollment.flatMap(this::toDto);
    }

    @Override
    public List<EnrollmentDto> findByStudentId(String studentId) {
        EntityId studentEntityId = EntityId.fromString(studentId);
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollmentRepository.findAll()) {
            if (enrollment.studentId().equals(studentEntityId)) {
                toDto(enrollment).ifPresent(enrollments::add);
            }
        }
        return enrollments;
    }

    @Override
    public List<EnrollmentDto> findByCourseId(String courseId) {
        EntityId courseEntityId = EntityId.fromString(courseId);
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollmentRepository.findAll()) {
            if (enrollment.courseId().equals(courseEntityId)) {
                toDto(enrollment).ifPresent(enrollments::add);
            }
        }
        return enrollments;
    }

    @Override
    public EnrollmentDto create(String studentId, String courseId, String grade) {
        EntityId studentEntityId = EntityId.fromString(studentId);
        EntityId courseEntityId = EntityId.fromString(courseId);

        if (studentRepository.findById(studentEntityId).isEmpty()) {
            throw new IllegalArgumentException("Student " + studentId + " not found");
        }
        if (courseRepository.findById(courseEntityId).isEmpty()) {
            throw new IllegalArgumentException("Course " + courseId + " not found");
        }

        EntityId enrollmentId = EntityId.random();
        String normalizedGrade = (grade == null) ? "" : grade;

        Enrollment enrollment = new Enrollment(enrollmentId, studentEntityId, courseEntityId, normalizedGrade);
        enrollmentRepository.insert(enrollment);

        return toDto(enrollment).orElseThrow();
    }

    @Override
    public EnrollmentDto update(String id, String grade) {
        EntityId entityId = EntityId.fromString(id);
        Optional<Enrollment> existingOpt = enrollmentRepository.findById(entityId);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Enrollment " + id + " not found");
        }

        Enrollment existing = existingOpt.get();
        String normalizedGrade = (grade == null) ? "" : grade;

        Enrollment updated = new Enrollment(existing.id(), existing.studentId(), existing.courseId(), normalizedGrade);
        enrollmentRepository.update(updated);

        return toDto(updated).orElseThrow();
    }

    @Override
    public void delete(String id) {
        EntityId entityId = EntityId.fromString(id);
        enrollmentRepository.delete(entityId);
    }

    private Optional<EnrollmentDto> toDto(Enrollment enrollment) {
        Optional<Student> optStudent = studentRepository.findById(enrollment.studentId());
        if (optStudent.isEmpty()) {
            return Optional.empty();
        }
        Student student = optStudent.get();

        Optional<Person> optPerson = personRepository.findById(student.personId());
        if (optPerson.isEmpty()) {
            return Optional.empty();
        }
        Person person = optPerson.get();

        Optional<Course> optCourse = courseRepository.findById(enrollment.courseId());
        if (optCourse.isEmpty()) {
            return Optional.empty();
        }
        Course course = optCourse.get();

        String grade = enrollment.grade();
        if (grade == null) {
            grade = "";
        }

        return Optional.of(new EnrollmentDto(
                enrollment.id().toString(),
                student.id().toString(),
                person.fullName(),
                course.id().toString(),
                course.code(),
                course.name(),
                grade
        ));
    }
}
