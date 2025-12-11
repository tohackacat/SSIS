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

public class DefaultEnrollmentService implements EnrollmentService {
    //Attributes
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final PersonRepository PersonRepository;
    private final CourseRepository courseRepository;

    public DefaultEnrollmentService(EnrollmentRepository enrollmentRepository,
                                    StudentRepository studentRepository,
                                    PersonRepository personRepository,
                                    CourseRepository courseRepository){
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;

    }


    //Finders
    @Override
    public List<EnrollmentDto> findAll(){
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for(Enrollment enrollment : enrollmentRepository.findAll()){
            <Optional<EnrollmentDto> enrollmentDto = toDto(enrollment);
            enrollmentDto.ifPresent(enrollments::add);
        }
        return enrollments;
    }

    @Override
    public Optional<EnrollmentDto> findById(String id){
        EntityId entityId = EntityId.fromString(id);
        Optional<Enrollment> enrollment = enrollmentRepository.findbyId(entityId);
        if(enrollment.isEmpty()){
            return Optional.empty();
        }
        return toDto(enrollment.get());

    }

    @Override
    public List<EnrollmentDto> findByStudentId(String studentId){
        EntityId studenEntityId - EntityId.fromString(studentId);
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for(Enrollment enrollment : enrollmentRepository.findAll()){
            if(enrollment.studentId().equals(studenEntityId)){
                toDto(enrollment).ifPresent(enrollments::add);
            }
        }
        return enrollments;
    }

    @Override
    public List<EnrollmentDto> findByCourseId(String courseId){
        EntityId courseEntityId - EntityId.fromString(courseId);
        List<EnrollmentDto> enrollments = new ArrayList<>();
        for(Enrollment enrollment : enrollmentRepository.findAll()){
            if(enrollment.courseId().equals(courseEntityId)){
                toDto(enrollment).ifPresent(enrollments::add);
            }
        }
        return enrollments;        
    }
 
    @Override
    public EnrollmentDto create(String studentId, String courseId, String grade){
        EntityId studentEntityId = EntityId.fromString(studentId);
        EntityId courseEntityId = EntityId.fromString(courseId);

        Optional<Student> optStudent = studentRepository.findById(studentEntityId);
        if(optStudent.isEmpty()){
            throw new IllegalArgumentException("Student " + studentId + " not found");
        }
        
        Optional<Course> optCourse = courseRepository.findById(courseEntityId);
        if(optCourse.isEmpty()){
            throw new IllegalArgumentException("Course " + courseId + " not found");
        }

        EntityId enrollmentId = EntityId.random();
        if(grade == null){
            grade = "";
        }

        Enrollment enrollment = new Enrollment(enrollmentId,studentEntityId,courseEntityId,grade);
        enrollmentRepository.insert(enrollment);

        return toDto(enrollment).orElseThrow();
    }
    
    @Override
    public EnrollmentDto update(String id, String grade){
        EntityId entityId = EntityId.fromString(id);
        Optional<Enrollment> optEnrollment = enrollmentRepository.findbyId(entityId);
        if(optEnrollment.isEmpty()){
            throw new IllegalArgumentException("Enrollment " + id + " not found");
        }

        Enrollment existing = optEnrollment.get();
        if(grade == null){
            grade = "";
        }

        Enrollment updatedEnrollment = new Enrollment(existing.id(), existing.studentId(), existing.courseId(), grade);
        enrollmentRepository.update(updatedEnrollment);
        
        return toDto(updatedEnrollment).orElseThrow();
    }
 
    public void delete(String id){
        EntityId entityId = EntityId.fromString(id);
        enrollmentRepository.delete(entityId);
    }

    private Optional<EnrollmentDto> toDto(Enrollment enrollment){
        Optional<Student> optStudent = studentRepository.findById(enrollment.studentId());
        if(optStudent.isEmpty()){
            return Optional.empty();
        }
        Student student = optStudent.get();

        Optional<Person> optPerson = personRepository.findById(student.personId());
        if(optPerson.isEmpty()){
            return Optional.empty();
        }
        Person person = optPerson.get();

        Optional<Course> optCourse = courseRepository.findById(enrollment.courseId());
        if(optCourse.isEmpty()){
            return Optional.empty();
        }
        Course course = optCourse.get();

        String grade = enrollment.grade();
        if(grade == null){
            grade = "";
        }


        return Optional.of(new EnrollmentDto(
                            enrollment.id().toString(), 
                            student.id().toString(), 
                            person.fullName(), //studentName
                            course.id().toString(), 
                            course.code(),
                            course.name(),
                            grade));
    }
    
}
