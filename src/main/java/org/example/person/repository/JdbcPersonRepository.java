package org.example.person.repository;

import org.example.db.DatabaseProvider;
import org.example.person.Person;
import org.example.person.PersonRepository;
import org.example.core.EntityId;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPersonRepository implements PersonRepository {
    private final DatabaseProvider provider;

    public JdbcPersonRepository(DatabaseProvider provider) {
        this.provider = provider;
    }

    @Override
    public Iterable<Person> findAll() {
        List<Person> result = new ArrayList<>();
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, full_name, email FROM persons ORDER BY full_name")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load persons", e);
        }
        return result;
    }

    @Override
    public Optional<Person> findById(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, full_name, email FROM persons WHERE id = ?")) {
            statement.setString(1, id.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load person " + id, e);
        }
    }

    @Override
    public void insert(Person person) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO persons (id, full_name, email) VALUES (?, ?, ?)")) {
            statement.setString(1, person.id().toString());
            statement.setString(2, person.fullName());
            if (person.email().isPresent()) {
                statement.setString(3, person.email().get());
            } else {
                statement.setNull(3, Types.VARCHAR);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert person", e);
        }
    }

    @Override
    public void update(Person person) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE persons SET full_name = ?, email = ? WHERE id = ?")) {
            statement.setString(1, person.fullName());
            if (person.email().isPresent()) {
                statement.setString(2, person.email().get());
            } else {
                statement.setNull(2, Types.VARCHAR);
            }
            statement.setString(3, person.id().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update person", e);
        }
    }

    @Override
    public void delete(EntityId id) {
        try (Connection connection = provider.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM persons WHERE id = ?")) {
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete person " + id, e);
        }
    }

    private Person mapRow(ResultSet rs) throws SQLException {
        String idText = rs.getString("id");
        String fullName = rs.getString("full_name");
        String emailText = rs.getString("email");
        EntityId id = EntityId.fromString(idText);
        java.util.Optional<String> email = emailText == null
                ? java.util.Optional.empty()
                : java.util.Optional.of(emailText);
        return new Person(id, fullName, email);
    }
}

    

