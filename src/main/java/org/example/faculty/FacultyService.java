package org.example.faculty;

import org.example.faculty.dto.FacultyDto;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    List<FacultyDto> findAll();

    Optional<FacultyDto> findById(String id);

    FacultyDto create(String fullName, String email, String department, double rate, double hours);

    FacultyDto update(String id, String fullName, String email, String department, double rate, double hours);

    void delete(String id);

}
