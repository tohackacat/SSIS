package org.example.faculty;

import org.example.faculty.dto.FacultyDto;

import java.util.List;
import java.util.Optional;

public final class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    public List<FacultyDto> listFaculty() {
        return service.findAll();
    }

    public Optional<FacultyDto> getFaculty(String id) {
        return service.findById(id);
    }

    public FacultyDto createFaculty(String fullName, String email, String department, double rate, double hours) {
        return service.create(fullName, email, department, rate, hours);
    }

    public FacultyDto updateFaculty(String id, String fullName, String email, String department, double rate, double hours) {
        return service.update(id, fullName, email, department, rate, hours);
    }

    public void deleteFaculty(String id) {
        service.delete(id);
    }

}
