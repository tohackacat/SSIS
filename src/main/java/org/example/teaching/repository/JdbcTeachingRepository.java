package org.example.teaching.repository;

import org.example.core.EntityId;
import org.example.db.DatabaseProvider;
import org.example.teaching.Teaching;
import org.example.teaching.TeachingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTeachingRepository implements TeachingRepository {
    private final DatabaseProvider provider;

    public JdbcTeachingRepository(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Teaching> findAll() {
        List<Teaching> result = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, course_id, faculty_id, role FROM teachings ORDER BY id")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load teachings", e);
        }
        return result;
    }

    @Override
    public Optional<Teaching> findById(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, course_id, faculty_id, role FROM teachings WHERE id = ?")) {
            statement.setString(1, id.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load teaching " + id, e);
        }
    }

    @Override
    public void insert(Teaching teaching) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO teachings (id, course_id, faculty_id, role) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, teaching.id().toString());
            statement.setString(2, teaching.courseId().toString());
            statement.setString(3, teaching.facultyId().toString());
            statement.setString(4, teaching.role());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert teaching", e);
        }
    }

    @Override
    public void update(Teaching teaching) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE teachings SET course_id = ?, faculty_id = ?, role = ? WHERE id = ?")) {
            statement.setString(1, teaching.courseId().toString());
            statement.setString(2, teaching.facultyId().toString());
            statement.setString(3, teaching.role());
            statement.setString(4, teaching.id().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update teaching", e);
        }
    }

    @Override
    public void delete(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM teachings WHERE id = ?")) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete teaching " + id, e);
        }
    }

    private Teaching mapRow(ResultSet rs) throws SQLException {
        String idText = rs.getString("id");
        String courseIdText = rs.getString("course_id");
        String facultyIdText = rs.getString("faculty_id");
        String role = rs.getString("role");
        EntityId id = EntityId.fromString(idText);
        EntityId courseId = EntityId.fromString(courseIdText);
        EntityId facultyId = EntityId.fromString(facultyIdText);
        return new Teaching(id, courseId, facultyId, role);
    }
}
