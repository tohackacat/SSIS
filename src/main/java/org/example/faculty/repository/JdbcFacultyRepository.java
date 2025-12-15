package org.example.faculty.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.core.EntityId;
import org.example.db.DatabaseProvider;
import org.example.faculty.Faculty;
import org.example.faculty.FacultyRepository;

public class JdbcFacultyRepository implements FacultyRepository {
    private final DatabaseProvider provider;

    public JdbcFacultyRepository(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Faculty> findAll() {
        List<Faculty> result = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, person_id, department, rate * hours AS salary FROM faculty ORDER BY department, id")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load faculty", e);
        }
        return result;
    }

    @Override
    public Optional<Faculty> findById(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, person_id, department, rate * hours AS salary, FROM faculty WHERE id = ?")) {
            statement.setString(1, id.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load faculty " + id, e);
        }
    }

    @Override
    public void insert(Faculty faculty) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO faculty (id, person_id, department, rate, hours) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, faculty.id().toString());
            statement.setString(2, faculty.personId().toString());
            statement.setString(3, faculty.department());
            statement.setDouble(4, faculty.rate());
            statement.setDouble(5, faculty.hours());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert faculty", e);
        }
    }

    @Override
    public void update(Faculty faculty) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE faculty SET person_id = ?, department = ?, rate = ?, hours = ? WHERE id = ?")) {
            statement.setString(1, faculty.personId().toString());
            statement.setString(2, faculty.department());
            statement.setDouble(3, faculty.rate());
            statement.setDouble(4, faculty.hours());
            statement.setString(5, faculty.id().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update faculty", e);
        }
    }

    @Override
    public void delete(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM faculty WHERE id = ?")) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete faculty " + id, e);
        }
    }

    private Faculty mapRow(ResultSet rs) throws SQLException {
        EntityId entityId = EntityId.fromString(rs.getString("id"));
        EntityId personEntityId = EntityId.fromString(rs.getString("person_id"));
        String department = rs.getString("department");
        double rate = rs.getDouble("rate");
        double hours = rs.getDouble("hours");
        return new Faculty(entityId, personEntityId, department, rate, hours);
    }
}
