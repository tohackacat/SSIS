package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public record DatabaseProvider(String url) {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

}
