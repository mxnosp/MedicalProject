package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EntryController {

    @FXML
    private void handleEnter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/mainscreen.fxml")
            );

            Scene scene = new Scene(loader.load(), 1100, 700);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Patients Dashboard");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load patient dashboard", e);
        }
    }
}