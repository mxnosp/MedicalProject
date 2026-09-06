package gui.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Patient;
import model.SmokingStatus;
import model.Vaccine;
import service.VaccineService;
import utils.DateParser;
import utils.NumberInputHelpers;

import java.util.Objects;

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
    private final ObservableList<Vaccine> vaccines = FXCollections.observableArrayList();
    private VaccineService vaccineService;

    @FXML
    private TableView<Vaccine> vaccinesTable;

    @FXML
    private TableColumn<Vaccine,String> vaccineTypeColumn;

    @FXML
    private TableColumn<Vaccine,String> vaccineDateColumn;

    @FXML
    private TableColumn<Vaccine,Number> vaccineDoseColumn;

    @FXML
    private TableColumn<Vaccine,Void> deleteVaccineColumn;

    @FXML
    private TableColumn<Vaccine,Number> vaccineIdColumn;

    @FXML private Button backButton;

    @FXML private void initialize(){
        vaccineService=new VaccineService();
        initializeTableColumns();
    }

    @FXML private void cancelForm(){
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    /**
     * initializes the vaccine table's columns
     */
    private void initializeTableColumns() {
        vaccineTypeColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName())
        );

        vaccineDoseColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShotnumber()));

        vaccineDateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(DateParser.getStringDate(cellData.getValue().getDate()))
        );

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
        loadVaccines();
    }

    private void loadVaccines(){
        vaccines.clear();
        vaccines.setAll(vaccineService.getPatientVaccines(this.selectedPatient.getPatientId()));
        vaccinesTable.setItems(vaccines);
    }
}
