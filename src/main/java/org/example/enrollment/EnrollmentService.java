package org.example.enrollment;

import org.example.enrollment.dto.EnrollmentDto;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    List<EnrollmentDto> findAll();

    Optional<EnrollmentDto> findById(String id);

    List<EnrollmentDto> findByStudentId(String studentId);

    List<EnrollmentDto> findByCourseId(String courseId);

    EnrollmentDto create(String studentId, String courseId, String grade);

    EnrollmentDto update(String id, String grade);

    void delete(String id);
}
