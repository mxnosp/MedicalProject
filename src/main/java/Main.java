import db.DBInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.scene.image.Image;
import service.BackupService;
import utils.DatabaseChangeTracker;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DBInitializer.initializeDB();
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/views/entryscene.fxml")
        );
        BackupService backupService=new BackupService("jdbc:sqlite:"+Paths.get(System.getProperty("user.home"), "medical-app/data", "medical_app.db").toString(),Paths.get(
                System.getProperty("user.home"),
                "medical-app",
                "backups"
        ));

        Scene scene = new Scene(loader.load(), 1600, 900);
        Image icon = new Image(getClass().getResourceAsStream("/images/doctorlogo.png"));
        stage.getIcons().add(icon);
        stage.setOnCloseRequest(event -> {
            try {
                if (DatabaseChangeTracker.hasChanged()) {
                    backupService.createBackup();
                    DatabaseChangeTracker.reset();
                }
            } catch (Exception _) {}
        });
        stage.setTitle("Σύστημα Διαχείρισης Ασθενών");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}