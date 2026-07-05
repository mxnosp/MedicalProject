package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OneDriveLocator {

    private OneDriveLocator() {}


    public static List<Path> findOneDriveDirectories() {
        List<Path> paths = new ArrayList<>();

        addIfValid(paths, System.getenv("OneDriveConsumer"));
        addIfValid(paths, System.getenv("OneDriveCommercial"));
        addIfValid(paths, System.getenv("OneDrive"));

        return paths;
    }

    private static void addIfValid(List<Path> paths, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        Path path = Path.of(value);

        if (Files.isDirectory(path) && !paths.contains(path)) {
            paths.add(path);
        }
    }
}