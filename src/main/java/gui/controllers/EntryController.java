package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class EntryController {
    @FXML
    private ImageView backgroundImage;

    @FXML
    private void initialize() {

        backgroundImage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

    }
    @FXML
    private void handleEnter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/mainscreen.fxml")
            );

            Scene scene = new Scene(loader.load(), 1600, 900);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Patients Dashboard");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load patient dashboard", e);
        }
    }
}