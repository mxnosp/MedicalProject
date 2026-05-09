package gui.controllers;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Date;
import model.Patient;
import model.Visit;
import service.PatientService;
import service.VisitService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PatientDashboardController {
    @FXML
    private Label searchErrorLabel;

    private PatientService patientService;

    private VisitService visitService;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchPatient;

    @FXML
    private Label selectedPatientIdLabel;

    @FXML
    private Label selectedFirstNameLabel;

    @FXML
    private Label selectedLastNameLabel;

    @FXML
    private Label selectedPhoneLabel;

    @FXML
    private Label selectedAmkaLabel;

    @FXML
    private Label selectedHeightLabel;

    @FXML
    private Label selectedWeightLabel;

    @FXML
    private Label selectedBMILabel;

    @FXML
    private Label selectedSmokerLabel;

    @FXML
    private Button morePatientInfoButton;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient,Number> idColumn;

    @FXML
    private TableColumn<Patient,String> firstNameColumn;

    @FXML
    private TableColumn<Patient,String> lastNameColumn;

    @FXML
    private TableColumn<Patient,String> amkaColumn;

    @FXML
    private Button addPatient;

    @FXML
    private Button editPatient;

    @FXML
    private Button deletePatient;

    @FXML
    private TableView<Visit> visitTable;

    @FXML
    private TableColumn<Visit, Date> visitDateColumn;

    @FXML
    private TableColumn<Visit,Button> viewVisitColumn;

    @FXML
    private Button addVisit;

    @FXML
    private Button editVisit;

    @FXML
    private Button deleteVisit;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();

    public PatientDashboardController(){
        patientService=new PatientService();
        visitService=new VisitService();
    }

    /**
     * initializes the tables
     */
    @FXML
    private void initialize(){
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initializeTableColumns();
        loadPatients();
        initializePatientSelection();
    }

    private void initializePatientSelection(){
        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable,oldPatient, newPatient) -> {
                    if (newPatient == null) {
                        clearSelectedPatientLabels();
                        return;
                    }

                    showSelectedPatient(newPatient);
                });
    }

    /**
     * clears the selected patients info on the left
     */
    private void clearSelectedPatientLabels(){
        selectedPatientIdLabel.setText("-");
        selectedAmkaLabel.setText("-");
        selectedPhoneLabel.setText("-");
        selectedBMILabel.setText("-");
        selectedSmokerLabel.setText("-");
        selectedWeightLabel.setText("-");
        selectedHeightLabel.setText("-");
        selectedFirstNameLabel.setText("-");
        selectedLastNameLabel.setText("-");
    }

    /**
     * shows the info of the selected patient in the left of the screen
     * @param selectedPatient
     */
    private void showSelectedPatient(Patient selectedPatient){
        selectedPatientIdLabel.setText(String.valueOf(selectedPatient.getPatientId()));
        selectedAmkaLabel.setText(selectedPatient.getPatientAmka());
        if(selectedPatient.getPatientPhone()!=null && !selectedPatient.getPatientPhone().isEmpty()){
            selectedPhoneLabel.setText(selectedPatient.getPatientPhone());
        }else selectedPhoneLabel.setText("-");
        selectedFirstNameLabel.setText(selectedPatient.getPatientFirstName());
        selectedLastNameLabel.setText(selectedPatient.getPatientLastName());
        selectedBMILabel.setText("-");
        selectedSmokerLabel.setText("-");
        selectedWeightLabel.setText("-");
        selectedHeightLabel.setText("-");
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getPatientId())
        );

        firstNameColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getPatientFirstName())
        );

        lastNameColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getPatientLastName())
        );

        amkaColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getPatientAmka())
        );
    }

    private void loadPatients(){
        patientTable.setItems(patients);
        patients.clear();
        patients.addAll(patientService.getAllPatients());
    }

    /**
     * Searches the Patient table
     */
    @FXML
    void searchPatient(){
        List<Patient> results=patientService.searchPatientsByName(searchField.getText());
        patientTable.setItems(patients);
        patients.clear();
        patients.setAll(results);
    }


    /**
     * Called when the more info button is  pressed
     */
    @FXML
    void showFUllPatientInfo(){

    }

    /**
     * opens a subwindow that has a form with the new patient's info
     * and a save button  to save the new patient to the table
     */
    @FXML
    private void openNewPatientForm(ActionEvent event){
         try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/patientscreen.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root, 700, 950);

            Stage patientStage = new Stage();
            patientStage.setTitle("Κάρτα Ασθενή");
            patientStage.setScene(scene);

            Stage ownerStage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            patientStage.initOwner(ownerStage);
            patientStage.initModality(Modality.WINDOW_MODAL);

            patientStage.showAndWait();

            loadPatients();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load patient form", e);
        }

    }

    /**
     * deletes the selected patient from the patient list and his visits
     */
    @FXML
    void deleteSelectedPatient(){
        if(Objects.equals(selectedPatientIdLabel.getText(), "-")){
            return;
        }
        patientService.deletePatient(Integer.parseInt(selectedPatientIdLabel.getText()));
        visitService.deletePatientVisits(Integer.parseInt(selectedPatientIdLabel.getText()));
        clearSelectedPatientLabels();
        loadPatients();
        clearVisits();
    }

    private void clearVisits(){

    }
    private void loadVisits(){


    }

    /**
     * opens a subwindow containing the selected patient's
     * info in order for the doctor to edit them
     */
    @FXML
    void openSelectedPatientForm(){}

    /**
     * opens a subwindow that has a form with the new visit's info
     * and a save button to to save the new visit to the table
     */
    @FXML
    void openNewVisitForm(){

    }

    /**
     * opens a subwindow that has a form with the selected visit's info
     * so the doctor can edit them
     */
    @FXML
    void openSelectedVisitForm(){

    }

    /**
     * deletes the selected visit form the visit table of the selected patient
     */
    @FXML
    void deleteSelectedVisit(){

    }

}