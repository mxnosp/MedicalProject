package service;

import utils.DirectoryUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static utils.DirectoryUtils.countFiles;
import static utils.DirectoryUtils.getOldestBackupFile;

/**
 * Makes a backupo  of the current database file and stores it with a timestamp
 */
public class BackupService {

    private final String databaseUrl;
    private final Path backupDirectory;

    public BackupService(String databaseUrl, Path backupDirectory) {
        this.databaseUrl = databaseUrl;
        this.backupDirectory = backupDirectory;
    }

    public Path createBackup() throws Exception {
        Files.createDirectories(backupDirectory);

        if(countFiles(backupDirectory)==10){
            deleteOldestBackup();
        }
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        Path backupPath = backupDirectory.resolve( timestamp + ".db");

        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement()) {

            String backupSql = "VACUUM INTO '" +
                    backupPath.toAbsolutePath().toString().replace("'", "''") +
                    "'";

            statement.execute(backupSql);
        }

        return backupPath;
    }

    private void deleteOldestBackup() {
        try{
            Optional<Path> oldest= getOldestBackupFile(backupDirectory);
            if(oldest.isPresent()){
                Files.delete(oldest.get());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}