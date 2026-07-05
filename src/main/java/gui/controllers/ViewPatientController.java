package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Patient;
import model.SmokingStatus;
import utils.NumberInputHelpers;

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

    @FXML private void cancelForm(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the fields of the selected patient
     * @param patient
     */
    public void setSelectedPatient(Patient patient){
        this.selectedPatient=patient;
        lastNameLabel.setText(selectedPatient.getPatientLastName());
        firstNameLabel.setText(selectedPatient.getPatientFirstName());
        phoneLabel.setText(selectedPatient.getPatientPhone());
        amkaLabel.setText(selectedPatient.getPatientAmka());
        SmokingStatus status=selectedPatient.getPatientSmokingStatus();
        smokingLabel.setText(status==null?"-":status.toString());
        heightLabel.setText(NumberInputHelpers.StringFromInteger(selectedPatient.getPatientHeight()));
        weightLabel.setText(NumberInputHelpers.StringFromInteger(selectedPatient.getPatientWeight()));
        bmiLabel.setText(NumberInputHelpers.StringFromDouble(selectedPatient.getPatientBMI()));
        medicalHistoryLabel.setText(selectedPatient.getPatientMedicalHistory());
        chronicMedicationLabel.setText(selectedPatient.getPatientChronicMedication());
        notesLabel.setText(selectedPatient.getPatientNotes());
    }
}
