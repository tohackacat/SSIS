package org.example.enrollment;

import java.util.List;
import java.util.Optional;

import org.example.enrollment.dto.EnrollmentDto;

public final class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service){
        this.service = service;
    }

    public List<EnrollmentDto> listEnrollments(){
        return service.findAll();
    }

    public List<EnrollmentDto> listEnrollmentsByStudent(String studentId){
        return service.findByStudentId(studentId);
    }

    public List<EnrollmentDto> listEnrollmentsByCourse(String courseId){
        return service.findByCourseId(courseId);
    }

    public Optional<EnrollmentDto> getEnrollment(String id){
        return service.findById(id);
    }

    public EnrollmentDto createEnrollment(String studentId, String courseId, String grade){
        return service.create(studentId, courseId, grade);
    }
    
    public EnrollmentDto updateEnrollment(String id, String grade){
        return service.update(id, grade);
    }

    public void deleteEnrollment(String id){
        service.delete(id);
    }

}
