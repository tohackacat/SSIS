package org.example.student;

import java.util.List;
import java.util.Optional;

import org.example.student.dto.StudentDto;

public interface StudentService {
    List<StudentDto> findAll();

    Optional<StudentDto> findById(String id);

    StudentDto create(String fullName, String email, int enrollmentYear);

    StudentDto update(String id, String fullName, String email, int enrollmentYear);

    void delete(String id);
}
