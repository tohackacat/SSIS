package org.example.enrollment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.example.core.EntityId;
import org.example.db.DatabaseProvider;
import org.example.enrollment.Enrollment;
import org.example.enrollment.EnrollmentRepository;

public class JdbcEnrollmentRepository implements EnrollmentRepository {
    private final DatabaseProvider provider;

    public JdbcEnrollmentRepository(DatabaseProvider provider){
        this.provider = provider;
    }

    @Override
    public Iterable<Enrollment> findAll(){
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "SELECT id, student_id, course_id, grade FROM enrollments ORDER BY id")){
                    try(ResultSet rs = statement.executeQuery()){
                        while(rs.next()){
                            enrollments.add(mapRow(rs));
                        }
                    } 
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load enrollments", e);
        }
        return enrollments;
    }
    
    @Override
    public Optional<Enrollment> findbyId(EntityId id){
        try(Connection connection = provider.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT id, student_id, course_id, grade FROM enrollments WHERE id = ?")){
                    statement.setString(1, id.toString());
                    try(ResultSet rs = statement.executeQuery()){
                        if(rs.next()){
                            return Optional.of(mapRow(rs));
                        }
                        return Optional.empty();
                    }
        } catch(SQLException e){
            throw new RuntimeException("Failed to load enrollment "+ id, e);
        }

    }

    @Override
    public void insert(Enrollment enrollment){
        try(Connection connection = provider.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO enrollments (id, student_id, course_id, grade) VALUES (?, ?, ?, ?)")){
                    statement.setString(1, enrollment.id().toString());
                    statement.setString(2, enrollment.studentId().toString());
                    statement.setString(3, enrollment.courseId().toString());
                    statement.setString(4, enrollment.grade());
                    statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Failed to insert enrollment " + enrollment.id().toString() , e);
        }
    }

    @Override
    public void update(Enrollment enrollment){
        try(Connection connection = provider.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE enrollments SET student_id = ?, course_id = ?, grade = ? WHERE id = ?")){
                    statement.setString(1, enrollment.studentId().toString());
                    statement.setString(2, enrollment.courseId().toString());
                    statement.setString(3, enrollment.grade());
                    statement.setString(4, enrollment.id().toString());
                    statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Failed to update enrollment " + enrollment.id().toString() , e);
        }
    }

    @Override
    public void delete(EntityId id){
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM enrollments WHERE id = ?")) {
            statement.setString(1,id.toString());
            statement.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete enrollment: " + id, e);
        }
    }
    
    private Enrollment mapRow(ResultSet rs) throws SQLException{
        String id = rs.getString("id");
        String studentId = rs.getString("student_id");
        String courseId = rs.getString("course_id");
        String grade = rs.getString("grade");
        EntityId entityId = EntityId.fromString(id);
        EntityId studentEntityId = EntityId.fromString(studentId);
        EntityId courseEntityId = EntityId.fromString(courseId);

        return new Enrollment(entityId, studentEntityId, courseEntityId, grade);
    }
}
