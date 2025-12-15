package org.example.teaching;
import java.util.List;
import java.util.Optional;

import org.example.teaching.dto.TeachingDto;

public interface  TeachingService {
    List<TeachingDto> findAll();

    Optional<TeachingDto> findById(String id);

    TeachingDto create(String courseId, String facultyId, String role);
    TeachingDto update(String id, String role);
    void delete(String id);

}
