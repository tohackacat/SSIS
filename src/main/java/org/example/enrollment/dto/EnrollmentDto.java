package org.example.enrollment.dto;

public record EnrollmentDto(String id,
                            String studentId,
                            String studentName,
                            String courseId,
                            String courseCode,
                            String courseName,
                            String grade) {

}
