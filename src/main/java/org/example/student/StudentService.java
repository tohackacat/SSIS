package org.example.student;

import org.example.student.dto.StudentDto;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentDto> findAll();

    Optional<StudentDto> findById(String id);

    StudentDto create(String fullName, String email, int enrollmentYear);

    StudentDto update(String id, String fullName, String email, int enrollmentYear);

    void delete(String id);
}
