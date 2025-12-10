package org.example.course.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.core.EntityId;
import org.example.course.Course;
import org.example.course.CourseRepository;

public class JdbcCourseRepository implements CourseRepository{
    private final DatabaseProvider provider;

    public JdbcCourseRepository(DatabaseProvider provider){
        this.provider = provider;
    }

    @Override
    public Iterable<Course> findAll(){
        List<Course> result = new ArrayList<>();
        try () {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public Optional<Course> findById(EntityId id);

    @Override
    public void insert(Course course);

    @Override
    public void update(Course course);

    @Override
    public void delete(EntityId id);
    
    
}
