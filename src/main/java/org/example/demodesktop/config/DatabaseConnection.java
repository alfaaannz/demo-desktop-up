package org.example.demodesktop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger log = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/javafxsample";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private DatabaseConnection() {
    }

    static {
        try {
            Class.forName(DB_DRIVER);
            log.info("Successfully initialized");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Database driver loading failed: {0}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getDbConnection() {
        try {
            return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Database connection failed: {0}", e.getMessage());
            return null;
        }
    }
}