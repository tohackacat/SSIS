package org.example.course.service;

import org.example.course.*;
import org.example.course.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.core.*;

public class DefaultCourseService implements CourseService {
    //Attributes
    private final CourseRepository repository; //just need the repository

    public DefaultCourseService(CourseRepository repository){
        this.repository = repository;
    }

    @Override
    public List<CourseDto> findAll(){
        List<CourseDto> result = new ArrayList<>(); //initialize with List as apractice incase of translating to LinkedList
        //populate list with all courses in repository
        for(Course course: repository.findAll()){
            result.add(toDto(course));
        }
        return result;
    }

    @Override
    public Optional<CourseDto> findById(String id){//optional return in case not found
        EntityId entityId = EntityId.fromString(id);
        Optional<Course> course = repository.findById(entityId);
        return course.map(this::toDto);//map() Optional method to apply toDto if not found returns null
    }

    @Override
    public CourseDto create(String code, String name, int credits){
        EntityId id = EntityId.random();
        Course course = new Course(id, code, name, credits);
        repository.insert(course);
        return toDto(course);

    }

    @Override
    public CourseDto update(String id, String code, String name, int credits){
        EntityId entityId = EntityId.fromString(id);//creates a new EntityId
        Optional<Course> courOptional = repository.findById(entityId);
        if(courOptional.isEmpty()){
            throw new IllegalArgumentException("Course ID: " + id + "not found");
        }
        Course updatedCourse = new Course(entityId, code, name, credits);
        repository.update(updatedCourse);
        return toDto(updatedCourse);

    }

    @Override
    public void delete(String id){
        EntityId entityId = EntityId.fromString(id);
        repository.delete(entityId);
    }


    private CourseDto toDto(Course course){
        return new CourseDto(
                    course.id().toString(),
                    course.code(),
                    course.name(),
                    course.credits()
        );

    }

    
}
