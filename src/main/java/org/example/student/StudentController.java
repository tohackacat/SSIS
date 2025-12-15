package org.example.student;

import org.example.student.dto.StudentDto;

import java.util.List;
import java.util.Optional;

public final class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    public List<StudentDto> listStudents() {
        return service.findAll();
    }

    public Optional<StudentDto> getStudent(String id) {
        return service.findById(id);
    }

    public StudentDto createStudent(String fullName, String email, int year) {
        return service.create(fullName, email, year);
    }

    public StudentDto updateStudent(String id, String fullName, String email, int year) {
        return service.update(id, fullName, email, year);
    }

    public void deleteStudent(String id) {
        service.delete(id);
    }
}
