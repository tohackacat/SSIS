package org.example.faculty;

import java.util.List;
import java.util.Optional;

import org.example.faculty.dto.FacultyDto;

public final class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service){
        this.service = service;
    }

    public List<FacultyDto> listFaculty(){
        return service.findAll();
    }

    public Optional<FacultyDto> getFaculty(String id){
        return service.findById(id);
    }

    public FacultyDto createFaculty(String fullName, String email, String department){
        return service.create(fullName, email, department);
    }

    public FacultyDto updateFaculty(String id, String fullname, String email, String department){
        return service.update(id, fullname, email, department);
    }

    public void deleteFaculty(String id){
        service.delete(id);
    }
    
}
