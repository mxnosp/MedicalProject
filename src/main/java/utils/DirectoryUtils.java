package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public class DirectoryUtils {

    public static long countFiles(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return 0;
        }

        try (var files = Files.list(directory)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(DirectoryUtils::isDatabaseFile)
                    .count();
        }
    }


    public static Optional<Path> getOldestBackupFile(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return Optional.empty();
        }

        try (var stream = Files.list(directory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(DirectoryUtils::isDatabaseFile)
                    .min(Comparator.comparing(DirectoryUtils::getLastModifiedInstant));
        }
    }

    private static boolean isDatabaseFile(Path path) {
        return path.getFileName().toString().endsWith(".db");
    }

    private static java.time.Instant getLastModifiedInstant(Path path) {
        try {
            return Files.getLastModifiedTime(path).toInstant();
        } catch (IOException e) {
            throw new RuntimeException("Could not read last modified time for: " + path, e);
        }
    }
}