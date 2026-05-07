package gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Date;
import model.Patient;
import model.Visit;

public class PatientDashboardController {

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
    private TableColumn<Patient,Integer> idColumn;

    @FXML
    private TableColumn<Patient,String> firstNameColumn;

    @FXML
    private TableColumn<Patient,String> lastNameColumn;

    @FXML
    private TableColumn<Patient,Integer> amkaColumn;

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


    /**
     * Searches the Patient table
     */
    @FXML
    void searchPatient(){

    }


    /**
     * Called when the more info button is  pressed
     */
    @FXML
    void showFUllPatientInfo(){

    }

    /**
     * opens a subwindow that has a form with the new patient's info
     * and a save button to to save the new patient to the table
     */
    @FXML
    void openNewPatientForm(){

    }

    /**
     * deletes the selected patient from the patient list
     */
    @FXML
    void deleteSelectedPatient(){

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
    void deleteSelectedVisiit(){

    }

}