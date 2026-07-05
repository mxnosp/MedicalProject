package utils;

/**
 * tracks if anything changed in the db while using the app in order to know whether
 * we should back up before closing
 */
public class DatabaseChangeTracker {
    public static boolean changed = false;

    public static void markChanged() {
        changed = true;
    }

    public static boolean hasChanged() {
        return changed;
    }

    public static void reset() {
        changed = false;
    }
}