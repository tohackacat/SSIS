package org.example.course;

import org.example.core.EntityId;
import java.util.Optional;


public interface CourseRepository {
    Iterable<Course> findAll();//return an iterable (broader scope than list, can be reiterated over)

    Optional<Course> findById(EntityId id);

    void insert(Course course);

    void update(Course course);

    void delete(EntityId id);
    
}
