package org.example.enrollment.dto;

import org.example.core.EntityId;

public record EnrollmentDto(String id, 
                            String studentId, 
                            String studentName,
                            String courseId, 
                            String courseCode,
                            String courseName,
                            String grade){
    
}
