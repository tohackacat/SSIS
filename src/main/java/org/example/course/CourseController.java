package org.example.course;

import org.example.course.dto.CourseDto;

import java.util.List;
import java.util.Optional;

public final class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    public List<CourseDto> listCourses() {
        return service.findAll();
    }

    public Optional<CourseDto> getCourse(String id) {
        return service.findById(id);
    }

    public CourseDto createCourse(String code, String name, int credits) {
        return service.create(code, name, credits);
    }

    public CourseDto updateCourse(String id, String code, String name, int credits) {
        return service.update(id, code, name, credits);
    }

    public void deleteCourse(String id) {
        service.delete(id);
    }

}
