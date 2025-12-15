package org.example.teaching;

import org.example.teaching.dto.TeachingDto;

import java.util.List;
import java.util.Optional;

public final class TeachingController {
    private final TeachingService service;

    public TeachingController(TeachingService service) {
        this.service = service;
    }

    public List<TeachingDto> listTeachings() {
        return service.findAll();
    }

    public Optional<TeachingDto> getTeaching(String id) {
        return service.findById(id);
    }

    public TeachingDto createTeaching(String courseId, String facultyId, String role) {
        return service.create(courseId, facultyId, role);
    }

    public TeachingDto updateTeaching(String id, String role) {
        return service.update(id, role);
    }

    public void deleteTeaching(String id) {
        service.delete(id);
    }
}

