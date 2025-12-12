package org.example.student.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.core.EntityId;
import org.example.db.DatabaseProvider;
import org.example.student.Student;
import org.example.student.StudentRepository;

public class JdbcStudentRepository implements StudentRepository {
     private final DatabaseProvider provider;

    public JdbcStudentRepository(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Student> findAll() {
        List<Student> result = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, person_id, enrollment_year FROM students ORDER BY enrollment_year, id")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load students", e);
        }
        return result;
    }

    @Override
    public Optional<Student> findById(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, person_id, enrollment_year FROM students WHERE id = ?")) {
            statement.setString(1, id.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load student " + id, e);
        }
    }

    @Override
    public void insert(Student student) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO students (id, person_id, enrollment_year) VALUES (?, ?, ?)")) {
            statement.setString(1, student.id().toString());
            statement.setString(2, student.personId().toString());
            statement.setInt(3, student.enrollmentYear());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert student", e);
        }
    }

    @Override
    public void update(Student student) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE students SET person_id = ?, enrollment_year = ? WHERE id = ?")) {
            statement.setString(1, student.personId().toString());
            statement.setInt(2, student.enrollmentYear());
            statement.setString(3, student.id().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    public void delete(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM students WHERE id = ?")) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student " + id, e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        String idText = rs.getString("id");
        String personIdText = rs.getString("person_id");
        int year = rs.getInt("enrollment_year");
        EntityId id = EntityId.fromString(idText);
        EntityId personId = EntityId.fromString(personIdText);
        return new Student(id, personId, year);
    }
}

