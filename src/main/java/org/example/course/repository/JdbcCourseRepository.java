package org.example.course.repository;

import org.example.core.EntityId;
import org.example.course.Course;
import org.example.course.CourseRepository;
import org.example.db.DatabaseProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCourseRepository implements CourseRepository {
    private final DatabaseProvider provider;

    public JdbcCourseRepository(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Course> findAll() {
        List<Course> result = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, code, name, credits FROM courses ORDER BY code")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load courses", e);
        }
        return result;
    }

    @Override
    public Optional<Course> findById(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, code, name, credits FROM courses WHERE id = ?")) {
            statement.setString(1, id.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load course " + id, e);
        }
    }

    @Override
    public void insert(Course course) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO courses (id, code, name, credits) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, course.id().toString());
            statement.setString(2, course.code());
            statement.setString(3, course.name());
            statement.setInt(4, course.credits());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert course", e);
        }
    }

    @Override
    public void update(Course course) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE courses SET code = ?, name = ?, credits = ? WHERE id = ?")) {
            statement.setString(1, course.code());
            statement.setString(2, course.name());
            statement.setInt(3, course.credits());
            statement.setString(4, course.id().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update course", e);
        }
    }

    @Override
    public void delete(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM courses WHERE id = ?")) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete course " + id, e);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        EntityId entityId = EntityId.fromString(rs.getString("id"));
        String code = rs.getString("code");
        String name = rs.getString("name");
        int credits = rs.getInt("credits");
        return new Course(entityId, code, name, credits);
    }
}
