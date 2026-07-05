import db.DBConnector;
import db.DBInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import service.BackupService;
import utils.DatabaseChangeTracker;

/**
 * Starts the JavaFX application from the executable jar
 */
public class Launcher {

    public static void main(String[] args) {
        Application.launch(MedicalApplication.class, args);
    }

    /**
     * Contains the application's JavaFX lifecycle
     */
    public static class MedicalApplication extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            DBInitializer.initializeDB();
            FXMLLoader loader = new FXMLLoader(
                    Launcher.class.getResource("/views/entryscene.fxml")
            );
            BackupService backupService = new BackupService(
                    DBConnector.getDatabasePath(),
                    DBConnector.getAppDirectoryPath().resolve("backups")
            );

            Scene scene = new Scene(loader.load(), 1600, 900);
            Image icon = new Image(getClass().getResourceAsStream("/images/doctorlogo.png"));
            stage.getIcons().add(icon);
            stage.setOnCloseRequest(event -> {
                try {
                    if (DatabaseChangeTracker.hasChanged()) {
                        backupService.createBackup();
                        DatabaseChangeTracker.reset();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            stage.setTitle("Σύστημα Διαχείρισης Ασθενών");
            stage.setScene(scene);
            stage.show();
        }
    }
}
