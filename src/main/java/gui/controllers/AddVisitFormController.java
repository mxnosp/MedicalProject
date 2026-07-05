package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Patient;
import service.VisitService;
import utils.DatabaseChangeTracker;
import utils.NumberInputHelpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddVisitFormController {
    private Patient selectedPatient;
    private final VisitService visitService;
    @FXML private DatePicker visitDatePicker;
    @FXML private DatePicker recheckDatePicker;

    @FXML private TextField reasonField;
    @FXML private TextField paymentField;

    @FXML private TextField spo2Field;
    @FXML private TextField pulseField;
    @FXML private TextField fev1Field;
    @FXML private TextField fvcField;
    @FXML private TextField pefField;
    @FXML private TextField fev1FvcField;
    @FXML private TextField fef2575Field;

    @FXML private TextArea physicalExamArea;
    @FXML private TextArea functionalExamArea;
    @FXML private TextArea treatmentArea;
    @FXML private TextArea notesArea;

    @FXML private Label formErrorLabel;

    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AddVisitFormController(){
        visitService=new VisitService();
    }

    @FXML
    private void initialize() {
        configureVisitDatePicker(visitDatePicker);
        configureVisitDatePicker(recheckDatePicker);
    }

    /**
     * configures the date pickers to be in the day/month/year format
     * @param calendar
     */
    private void configureVisitDatePicker(DatePicker calendar) {
        calendar.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate date) {
                if (date == null) {
                    return "";
                }

                return date.format(dateFormatter);
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.trim().isEmpty()) {
                    return null;
                }

                try {
                    return LocalDate.parse(text.trim(), dateFormatter);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });
    }
    @FXML
    private void savePressed(){
        if(checkNeccesaryFieldsFilled()) saveForm();
    }

    private boolean checkNeccesaryFieldsFilled(){
        // The date is the only mandatory visit field
        if(visitDatePicker.getValue()==null){
            visitDatePicker.getStyleClass().add("input-error");
            formErrorLabel.setText("Η ημερομηνία είναι υποχρεωτική!");
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
            return false;
        }else {
            visitDatePicker.getStyleClass().remove("input-error");
            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);
        }
        return true;
    }

    @FXML
    private void cancelForm(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void saveForm(){
        try{
            LocalDate date=visitDatePicker.getValue();
            String reason=reasonField.getText();
            Integer payment=NumberInputHelpers.parseInteger(paymentField.getText(), "Πληρωμή");
            Integer spo2=NumberInputHelpers.parseInteger(spo2Field.getText(), "SpO₂");
            Integer heartrate=NumberInputHelpers.parseInteger(pulseField.getText(), "Σφύξεις");
            Double fev1=NumberInputHelpers.parseDouble(fev1Field.getText(), "FEV1");
            Double fvc=NumberInputHelpers.parseDouble(fvcField.getText(), "FVC");
            Double pef=NumberInputHelpers.parseDouble(pefField.getText(), "PEF");
            Double fef2575=NumberInputHelpers.parseDouble(fef2575Field.getText(), "FEF25-75");
            String physicalCheck=physicalExamArea.getText();
            String functionalCheck=functionalExamArea.getText();
            String notes=notesArea.getText();
            LocalDate recheckDate=recheckDatePicker.getValue();
            String medication=treatmentArea.getText();
            visitService.insertVisit(notes,payment,date,selectedPatient.getPatientId(),fev1,fvc,pef,fef2575,heartrate,spo2,physicalCheck,functionalCheck,medication,reason,recheckDate);
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
    }
}
