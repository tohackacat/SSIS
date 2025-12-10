package org.example.course;

import java.util.List;
import java.util.Optional;

import org.example.course.dto.CourseDto;

public interface CourseService {

    List<CourseDto> findAll();
    Optional<CourseDto> findById(String id);

    CourseDto create(String code, String name, int credits);
    CourseDto update(String id, String code, String name, int credits);

    void delete(String id);
}