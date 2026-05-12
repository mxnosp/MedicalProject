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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Patient;
import model.Visit;
import service.PatientService;
import service.VisitService;
import utils.DateParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PatientDashboardController {
    private  Patient selectedPatient = null;
    private Visit selectedVisit = null;
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
    private TableColumn<Patient, Number> idColumn;

    @FXML
    private TableColumn<Patient, String> firstNameColumn;

    @FXML
    private TableColumn<Patient, String> lastNameColumn;

    @FXML
    private TableColumn<Patient, String> amkaColumn;

    @FXML
    private TableView<Visit> visitTable;

    @FXML
    private TableColumn<Visit, String> visitDateColumn;

    @FXML
    private TableColumn<Visit, Void> viewVisitColumn;

    @FXML
    private TableColumn<Visit, Integer> visitIdColumn;

    @FXML
    private Button addVisit;

    @FXML
    private Button editVisit;

    @FXML
    private Button deleteVisit;

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private final ObservableList<Visit> visits = FXCollections.observableArrayList();

    public PatientDashboardController() {
        patientService = new PatientService();
        visitService = new VisitService();
    }

    /**
     * initializes the tables
     */
    @FXML
    private void initialize() {
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initializeTableColumns();
        loadPatients();
        initializePatientSelection();
        initializeVisitSelection();
    }

    /**
     * sets the action that will be done when a row of the patient table is pressed
     */
    private void initializePatientSelection() {
        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldPatient, newPatient) -> {
                    if (newPatient == null) {
                        clearSelectedPatientLabels();
                        return;
                    }

                    showSelectedPatient(newPatient);
                    loadVisits();
                });
    }

    /**
     * sets the action that will be done when a row of the visit table is pressed
     */
    private void initializeVisitSelection() {
        visitTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldVisit, newVisit) -> {
                    if (newVisit == null) {
                        return;
                    }
                    selectedVisit = newVisit;
                });
    }

    /**
     * clears the selected patients info on the left
     */
    private void clearSelectedPatientLabels() {
        selectedPatientIdLabel.setText("-");
        selectedAmkaLabel.setText("-");
        selectedPhoneLabel.setText("-");
        selectedBMILabel.setText("-");
        selectedSmokerLabel.setText("-");
        selectedWeightLabel.setText("-");
        selectedHeightLabel.setText("-");
        selectedFirstNameLabel.setText("-");
        selectedLastNameLabel.setText("-");
        selectedPatient = null;
    }

    /**
     * shows the info of the selected patient in the left of the screen
     *
     * @param patientToShow
     */
    private void showSelectedPatient(Patient patientToShow) {
        clearSelectedPatientLabels();
        selectedPatientIdLabel.setText(String.valueOf(patientToShow.getPatientId()));
        selectedAmkaLabel.setText(patientToShow.getPatientAmka());
        if (patientToShow.getPatientPhone() != null && !patientToShow.getPatientPhone().isEmpty()) {
            selectedPhoneLabel.setText(patientToShow.getPatientPhone());
        }
        selectedFirstNameLabel.setText(patientToShow.getPatientFirstName());
        selectedLastNameLabel.setText(patientToShow.getPatientLastName());
        if (patientToShow.getPatientBMI() != null)
            selectedBMILabel.setText(Double.toString(patientToShow.getPatientBMI()));
        if (patientToShow.getPatientSmokingStatus() != null)
            selectedSmokerLabel.setText(patientToShow.getPatientSmokingStatus().toString());
        if (patientToShow.getPatientWeight() != null)
            selectedWeightLabel.setText(Integer.toString(patientToShow.getPatientWeight()) + "kg");
        if (patientToShow.getPatientHeight() != null)
            selectedHeightLabel.setText(Integer.toString(patientToShow.getPatientHeight()) + "cm");
        selectedPatient = new Patient(patientToShow.getPatientId(), patientToShow.getPatientFirstName(), patientToShow.getPatientLastName(), patientToShow.getPatientPhone(), patientToShow.getPatientAmka(), patientToShow.getPatientSmokingStatus() == null ? null : patientToShow.getPatientSmokingStatus().ordinal()
                , patientToShow.getPatientHeight(), patientToShow.getPatientWeight(), patientToShow.getPatientMedicalHistory(), patientToShow.getPatientChronicMedication(), patientToShow.getPatientNotes());
    }

    /**
     * initializes the patient and the visit table's columns
     */
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

        visitIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject()
        );

        visitDateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(DateParser.getStringDate(cellData.getValue().getVisitDate()))
        );

        viewVisitColumn.setCellFactory(column -> new TableCell<Visit, Void>() {

            private final Button viewButton = new Button();

            {
                viewButton.setOnAction(event -> {
                    Visit visit = getTableView().getItems().get(getIndex());
                    viewSelectedVisit(visit);
                });
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/view.png")));
                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(18);
                imageView.setFitHeight(18);
                imageView.setPreserveRatio(true);
                viewButton.setGraphic(imageView);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });
    }

    /**
     *loads the patients to the table
     */
    private void loadPatients() {
        patients.clear();
        patients.setAll(patientService.getAllPatients());
        patientTable.setItems(patients);
    }

    /**
     * Searches the Patient table
     */
    @FXML
    void searchPatient() {
        patientTable.setItems(patients);
        patients.clear();
        Patient searchedByAmka = patientService.searchPatientByAmka(searchField.getText());
        if (searchedByAmka != null) {
            patients.setAll(searchedByAmka);
        } else {
            List<Patient> results = patientService.searchPatientsByName(searchField.getText());
            patients.setAll(results);
        }
        loadVisits();
    }


    /**
     * Called when the more info button is  pressed
     */
    @FXML
    void showFUllPatientInfo(ActionEvent event) {
        if(selectedPatient==null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/viewpatientscreen.fxml")
            );

            Parent root = loader.load();
            ViewPatientController controller=loader.getController();
            controller.setSelectedPatient(selectedPatient);

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

        } catch (IOException e) {
            throw new RuntimeException("Failed to load patient form", e);
        }
    }

    /**
     * opens a subwindow that has a form with the new patient's info
     * and a save button  to save the new patient to the table
     */
    @FXML
    private void openNewPatientForm(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/addpatientscreen.fxml")
            );

            Parent root = loader.load();
            AddPatientFormController controller=loader.getController();
            controller.setSelectedPatient(selectedPatient);

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
    void deleteSelectedPatient() {
        if (selectedPatient == null) {
            return;
        }
        patientService.deletePatient(Integer.parseInt(selectedPatientIdLabel.getText()));
        visitService.deletePatientVisits(Integer.parseInt(selectedPatientIdLabel.getText()));
        clearSelectedPatientLabels();
        loadPatients();
        clearVisits();
        selectedPatient = null;
    }

    /**
     * clears the visits table
     */
    private void clearVisits() {
        visitTable.setItems(visits);
        visits.clear();
    }

    private void loadVisits() {
        visitTable.setItems(visits);
        visits.clear();
        if(selectedPatient!=null)  visits.addAll(visitService.getPatientVisits(selectedPatient.getPatientId()));
    }

    /**
     * opens a subwindow containing the selected patient's
     * info in order for the doctor to edit them
     */
    @FXML
    void openSelectedPatientForm(ActionEvent event) {
        if (selectedPatient == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/editpatientscreen.fxml")
            );

            Parent root = loader.load();
            EditPatientFormController controller=loader.getController();
            controller.setSelectedPatient(selectedPatient);
            Scene scene = new Scene(root, 700, 950);

            Stage patientStage = new Stage();
            patientStage.setTitle("Επεξεργασία Ασθενή");
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
     * opens a subwindow that has a form with the new visit's info
     * and a save button  to save the new visit to the table
     */
    @FXML
    private void openNewVisitForm(ActionEvent event) {
        if (selectedPatient == null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/addvisitscreen.fxml")
            );

            Parent root = loader.load();
            AddVisitFormController controller=loader.getController();
            controller.setSelectedPatient(selectedPatient);

            Scene scene = new Scene(root, 700, 950);

            Stage visitStage = new Stage();
            visitStage.setTitle("Προσθήκη Επίσκεψης");
            visitStage.setScene(scene);

            Stage ownerStage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            visitStage.initOwner(ownerStage);
            visitStage.initModality(Modality.WINDOW_MODAL);

            visitStage.showAndWait();

            loadVisits();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load visit form", e);
        }
    }

    /**
     * opens a subwindow that has a form with the selected visit's info
     * so the doctor can edit them
     */
    @FXML
    private void openSelectedVisitForm(ActionEvent event) {
        if(selectedVisit==null) return;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/editvisitscreen.fxml")
            );

            Parent root = loader.load();
            EditVisitFormController controller=loader.getController();
            controller.setSelectedVisit(selectedVisit);

            Scene scene = new Scene(root, 700, 950);

            Stage visitStage = new Stage();
            visitStage.setTitle("Επεξεργασία Επίσκεψης");
            visitStage.setScene(scene);

            Stage ownerStage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            visitStage.initOwner(ownerStage);
            visitStage.initModality(Modality.WINDOW_MODAL);

            visitStage.showAndWait();
            selectedVisit=null;
            loadVisits();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load visit form", e);
        }
    }

    /**
     * deletes the selected visit form the visit table of the selected patient
     */
    @FXML
    private void deleteSelectedVisit() {
        if(selectedVisit==null) return;
        visitService.deleteVisit(selectedVisit.getId());
        loadVisits();
    }

    /**
     * opens the selected visit's info for the user to just see them
     * @param visit
     */
    private void viewSelectedVisit( Visit visit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/viewvisitscreen.fxml")
            );

            Parent root = loader.load();

            ViewVisitController controller = loader.getController();
            controller.setVisit(visit);

            Scene scene = new Scene(root, 700, 950);

            Stage viewVisitStage = new Stage();
            viewVisitStage.setTitle("Προβολή Επίσκεψης");
            viewVisitStage.setScene(scene);

            Stage ownerStage = (Stage) visitTable
                    .getScene()
                    .getWindow();

            viewVisitStage.initOwner(ownerStage);
            viewVisitStage.initModality(Modality.WINDOW_MODAL);

            viewVisitStage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load view visit form", e);
        }

    }
}