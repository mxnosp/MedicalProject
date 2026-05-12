package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Patient;

public class ViewPatientController {
    private Patient selectedPatient;
    @FXML
    private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label amkaLabel;
    @FXML private Label phoneLabel;
    @FXML private Label smokingLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private Label bmiLabel;
    @FXML private Label medicalHistoryLabel;
    @FXML private Label chronicMedicationLabel;
    @FXML private Label notesLabel;

    @FXML private Button backButton;
}
