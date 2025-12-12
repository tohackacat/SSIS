package org.example.teaching.dto;

public record TeachingDto (
    String id,
    String courseId,
    String courseCode,
    String courseName,
    String facultyId,
    String facultyName,
    String role
)
{}
