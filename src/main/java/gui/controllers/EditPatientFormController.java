package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Patient;
import model.SmokingStatus;
import service.PatientService;
import utils.DatabaseChangeTracker;
import utils.NumberInputHelpers;

public class EditPatientFormController {
    private Patient selectedPatient;
    private PatientService patientService;

    @FXML
    private Label formErrorLabel;

    @FXML
    private TextArea notesArea;

    @FXML
    private TextArea chronicMedicationArea;

    @FXML
    private TextArea medicalHistoryArea;

    @FXML
    private TextField bmiField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private ComboBox<SmokingStatus> smokingComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField amkaField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize(){
        patientService=new PatientService();
    }

    /**
     * closes the patient form window
     */
    @FXML
    private void cancelForm(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void savePressed(){
        if(checkNeccesaryFieldsFilled()) saveForm();
    }

    /**
     * checks and returns true if all the necessary fields have been field if they haven't  paints
     * them red
     * @return
     */
    private boolean checkNeccesaryFieldsFilled() {
        boolean allfilled=true;
        if(firstNameField.getText().isEmpty()){
            firstNameField.getStyleClass().add("input-error");
            allfilled=false;
        }else firstNameField.getStyleClass().remove("input-error");

        if(lastNameField.getText().isEmpty()){
            lastNameField.getStyleClass().add("input-error");
            allfilled=false;
        }else lastNameField.getStyleClass().remove("input-error");
        if(amkaField.getText().isEmpty()){
            amkaField.getStyleClass().add("input-error");
            allfilled=false;
        }else amkaField.getStyleClass().remove("input-error");
        if(!allfilled){
            formErrorLabel.setText("Συμπληρώστε τα υποχρεωτικά πεδία!");
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }
        return allfilled;
    }


    /**
     * Save form function creates a patient with the information given by the user
     * if any errors occur it updates the error label
     */
    private void saveForm() {
        try{
            String firstname=firstNameField.getText();
            String lastname=lastNameField.getText();
            String amka=amkaField.getText();
            String phone=phoneField.getText();
            SmokingStatus smokingStatus=smokingComboBox.getValue();
            Integer height=NumberInputHelpers.parseInteger(heightField.getText(), "Ύψος");
            Integer weight=NumberInputHelpers.parseInteger(weightField.getText(), "Βάρος");
            String medicalHistory=medicalHistoryArea.getText();
            String chronicMedication=chronicMedicationArea.getText();
            String notes=notesArea.getText();
            patientService.updatePatient(firstname,lastname,phone,amka,smokingStatus,height,weight,medicalHistory,chronicMedication,notes,selectedPatient.getPatientId());
            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);
            DatabaseChangeTracker.markChanged();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e){
            formErrorLabel.setText(e.getMessage());
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }


    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
        smokingComboBox.getItems().setAll(SmokingStatus.values());
        lastNameField.setText(selectedPatient.getPatientLastName());
        firstNameField.setText(selectedPatient.getPatientFirstName());
        phoneField.setText(selectedPatient.getPatientPhone());
        amkaField.setText(selectedPatient.getPatientAmka());
        smokingComboBox.setValue(selectedPatient.getPatientSmokingStatus());
        heightField.setText(NumberInputHelpers.StringFromInteger(selectedPatient.getPatientHeight()));
        weightField.setText(NumberInputHelpers.StringFromInteger(selectedPatient.getPatientWeight()));
        bmiField.setText(NumberInputHelpers.StringFromDouble(selectedPatient.getPatientBMI()));
        medicalHistoryArea.setText(selectedPatient.getPatientMedicalHistory());
        chronicMedicationArea.setText(selectedPatient.getPatientChronicMedication());
        notesArea.setText(selectedPatient.getPatientNotes());
    }
}
