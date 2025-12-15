package org.example.teaching;

import org.example.teaching.dto.TeachingDto;

import java.util.List;
import java.util.Optional;

public interface TeachingService {
    List<TeachingDto> findAll();

    Optional<TeachingDto> findById(String id);

    TeachingDto create(String courseId, String facultyId, String role);

    TeachingDto update(String id, String role);

    void delete(String id);

}
