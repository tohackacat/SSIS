package org.example.faculty;

import java.util.List;
import java.util.Optional;

import org.example.faculty.dto.FacultyDto;

public interface FacultyService {
    List<FacultyDto> findAll();

    Optional<FacultyDto> findById();

    FacultyDto create(String fullName, String email, String department);

    FacultyDto update(String id, String fullName, String email, String department);

    void delete(String id);

}
