package db;

import model.exceptions.DBCreationException;
import utils.OneDriveLocator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public final class DBConnector {

    private static final String APP_FOLDER_NAME = "medical-app";
    private static final String DATABASE_FILE_NAME = "medical_app.db";

    private static Path appDirectoryPath;
    private static Path dbPath;

    private DBConnector() {
    }

    public static Connection getConnection() throws SQLException {
        ensurePathsAreSet();
        createAppDirectoryIfNeeded();

        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();
        return DriverManager.getConnection(url);
    }

    public static String getUrl() {
        ensurePathsAreSet();
        createAppDirectoryIfNeeded();
        return "jdbc:sqlite:" + dbPath.toAbsolutePath();
    }

    public static Path getDatabasePath() {
        ensurePathsAreSet();
        return dbPath;
    }

    public static Path getAppDirectoryPath() {
        ensurePathsAreSet();
        return appDirectoryPath;
    }

    private static void ensurePathsAreSet() {
        if (appDirectoryPath == null || dbPath == null) {
            appDirectoryPath = resolveAppDirectoryPath();
            dbPath = appDirectoryPath.resolve(DATABASE_FILE_NAME);
        }
    }

    private static Path resolveAppDirectoryPath() {
        List<Path> oneDriveDirectories = OneDriveLocator.findOneDriveDirectories();

        if (!oneDriveDirectories.isEmpty()) {
            return oneDriveDirectories.get(0)
                    .resolve(APP_FOLDER_NAME);
        }

        return Paths.get(
                System.getProperty("user.home"),
                APP_FOLDER_NAME
        );
    }

    private static void createAppDirectoryIfNeeded() {
        try {
            Files.createDirectories(appDirectoryPath);
        } catch (Exception e) {
            throw new DBCreationException(
                    "Failed to create app directory: " + appDirectoryPath,
                    e
            );
        }
    }
}
