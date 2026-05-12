package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Patient;
import model.Visit;
import service.VisitService;
import utils.DateParser;
import utils.NumberInputHelpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditVisitFormController {
    private Visit selectedVisit;
    private final VisitService visitService;
    @FXML
    private DatePicker visitDatePicker;
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

    public EditVisitFormController(){
        visitService=new VisitService();
    }

   @FXML
   private void initialize(){
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

    /**
     * sets the selected visit
     * @param selectedVisit
     */
    public void setSelectedVisit(Visit selectedVisit){
        this.selectedVisit=selectedVisit;
        loadVisitFields();
    }

    /**
     * Loads the already filled fields of the selected visit
     */
    private void loadVisitFields(){
        visitDatePicker.setValue(selectedVisit.getVisitDate());
        reasonField.setText(selectedVisit.getReason());
        paymentField.setText(NumberInputHelpers.StringFromInteger(selectedVisit.getVisitPayment()));
        spo2Field.setText(NumberInputHelpers.StringFromInteger(selectedVisit.getSpo2()));
        pulseField.setText(NumberInputHelpers.StringFromInteger(selectedVisit.getHeartRate()));
        fev1Field.setText(NumberInputHelpers.StringFromDouble(selectedVisit.getSpirometry().getFEV1()));
        fvcField.setText(NumberInputHelpers.StringFromDouble(selectedVisit.getSpirometry().getFVC()));
        pefField.setText(NumberInputHelpers.StringFromDouble(selectedVisit.getSpirometry().getPEF()));
        fef2575Field.setText(NumberInputHelpers.StringFromDouble(selectedVisit.getSpirometry().getFEF2575()));
        if(selectedVisit.getSpirometry().getFEV1()!=null && selectedVisit.getSpirometry().getFVC()!=null){
            double division=selectedVisit.getSpirometry().getFEV1()/selectedVisit.getSpirometry().getFVC();
            fev1FvcField.setText(NumberInputHelpers.StringFromDouble(Math.round(division*100.0)/100.0));
        };
        physicalExamArea.setText(selectedVisit.getPhysicalCheck());
        functionalExamArea.setText(selectedVisit.getFunctionalCheck());
        treatmentArea.setText(selectedVisit.getMedication());
        notesArea.setText(selectedVisit.getVisitNotes());
        recheckDatePicker.setValue(selectedVisit.getReappoinment());

    }

    /**
     * closes the edit visit form window
     */
    @FXML
    private void cancelForm(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     *parses the data from the fields and updates the selected visit
     */
    @FXML
    private void savePressed(){
        try{
            LocalDate date=visitDatePicker.getValue();
            String reason=reasonField.getText();
            Integer payment=NumberInputHelpers.parseInteger(paymentField.getText());
            Integer spo2=NumberInputHelpers.parseInteger(spo2Field.getText());
            Integer heartrate=NumberInputHelpers.parseInteger(pulseField.getText());
            Double fev1=NumberInputHelpers.parseDouble(fev1Field.getText());
            Double fvc=NumberInputHelpers.parseDouble(fvcField.getText());
            Double pef=NumberInputHelpers.parseDouble(pefField.getText());
            Double fef2575=NumberInputHelpers.parseDouble(fef2575Field.getText());
            String physicalCheck=physicalExamArea.getText();
            String functionalCheck=functionalExamArea.getText();
            String notes=notesArea.getText();
            LocalDate recheckDate=recheckDatePicker.getValue();
            String medication=treatmentArea.getText();
            visitService.updateVisit(notes,payment,date,selectedVisit.getPatientid(),fev1,fvc,pef,fef2575,heartrate,spo2,physicalCheck,functionalCheck,medication,reason,recheckDate,selectedVisit.getId());
            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } catch (RuntimeException e){
            formErrorLabel.setText(e.getMessage());
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }
    }
}
