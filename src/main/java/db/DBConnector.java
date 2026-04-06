package db;

import model.exceptions.DBCreationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * DBConnector class includes the static method connect that we will use in order to connect to the db
 * it also lets us use a custom path to place the db in
 */
public final class DBConnector {

    private static Path dbPath = Paths.get(System.getProperty("user.home"), "medical-app", "medical_app.db");

    private DBConnector() {}

    public static void setDatabasePath(Path customPath) {
        if (customPath == null) {
            throw new DBCreationException("Database path cannot be null");
        }
        dbPath = customPath.toAbsolutePath();
    }

    public static Connection getConnection() throws SQLException {
        createParentDirectoryIfNeeded();

        String url = "jdbc:sqlite:" + dbPath;
        return DriverManager.getConnection(url);
    }

    private static void createParentDirectoryIfNeeded() {
        try {
            Path parent = dbPath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (Exception e) {
            throw new DBCreationException(
                    "Failed to create database directory for: " + dbPath, e
            );
        }
    }
}