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
import service.PatientService;
import service.VaccineService;
import utils.DatabaseChangeTracker;
import utils.DateParser;
import utils.NumberInputHelpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddPatientFormController {

    private static final double VACCINE_TABLE_HEIGHT = 150;

    private Patient selectedPatient;

    private VaccineService vaccineService;
    private PatientService patientService;

    private final ObservableList<Vaccine> vaccines =
            FXCollections.observableArrayList();

    // pseudoId -> Vaccine
    private final Map<Integer, Vaccine> vaccineBuffer =
            new LinkedHashMap<>();

    private int vaccineBufferCounter;

    @FXML
    private TableView<Vaccine> vaccinesTable;

    @FXML
    private TableColumn<Vaccine, String> vaccineTypeColumn;

    @FXML
    private TableColumn<Vaccine, String> vaccineDateColumn;

    @FXML
    private TableColumn<Vaccine, Number> vaccineDoseColumn;

    @FXML
    private TableColumn<Vaccine, Void> deleteVaccineColumn;

    @FXML
    private TableColumn<Vaccine, Number> vaccineIdColumn;

    @FXML
    private Label totalVaccinesLabel;

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
    private TextField vaccineTypeField;

    @FXML
    private DatePicker vaccineDatePicker;

    @FXML
    private TextField vaccineDoseField;

    @FXML
    private Button addVaccineButton;


    @FXML
    private void initialize() {

        vaccineBufferCounter = 0;

        vaccineService = new VaccineService();
        patientService = new PatientService();

        initializeTableColumns();
        configureVaccineTable();

        smokingComboBox.getItems().setAll(SmokingStatus.values());
    }


    private void configureVaccineTable() {

        // Set items ONCE
        vaccinesTable.setItems(vaccines);

        // Force identical table height in Add/Edit
        vaccinesTable.setMinHeight(VACCINE_TABLE_HEIGHT);
        vaccinesTable.setPrefHeight(VACCINE_TABLE_HEIGHT);
        vaccinesTable.setMaxHeight(VACCINE_TABLE_HEIGHT);

        // Disable sorting
        vaccineIdColumn.setSortable(false);
        vaccineTypeColumn.setSortable(false);
        vaccineDateColumn.setSortable(false);
        vaccineDoseColumn.setSortable(false);
        deleteVaccineColumn.setSortable(false);

        // Disable column dragging/reordering
        vaccineIdColumn.setReorderable(false);
        vaccineTypeColumn.setReorderable(false);
        vaccineDateColumn.setReorderable(false);
        vaccineDoseColumn.setReorderable(false);
        deleteVaccineColumn.setReorderable(false);

        vaccinesTable.getSortOrder().clear();
    }


    @FXML
    private void cancelForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void savePressed() {
        if (checkNeccesaryFieldsFilled()) {
            saveForm();
        }
    }


    private boolean checkNeccesaryFieldsFilled() {

        boolean allfilled = true;

        if (firstNameField.getText().isEmpty()) {
            firstNameField.getStyleClass().add("input-error");
            allfilled = false;
        } else {
            firstNameField.getStyleClass().remove("input-error");
        }

        if (lastNameField.getText().isEmpty()) {
            lastNameField.getStyleClass().add("input-error");
            allfilled = false;
        } else {
            lastNameField.getStyleClass().remove("input-error");
        }

        if (amkaField.getText().isEmpty()) {
            amkaField.getStyleClass().add("input-error");
            allfilled = false;
        } else {
            amkaField.getStyleClass().remove("input-error");
        }

        if (!allfilled) {
            formErrorLabel.setText(
                    "Συμπληρώστε τα υποχρεωτικά πεδία!"
            );

            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }

        return allfilled;
    }


    private void saveForm() {

        try {

            String firstname = firstNameField.getText();
            String lastname = lastNameField.getText();
            String amka = amkaField.getText();
            String phone = phoneField.getText();

            SmokingStatus smokingStatus =
                    smokingComboBox.getValue();

            Integer height =
                    NumberInputHelpers.parseInteger(
                            heightField.getText(),
                            "Ύψος"
                    );

            Integer weight =
                    NumberInputHelpers.parseInteger(
                            weightField.getText(),
                            "Βάρος"
                    );

            String medicalHistory =
                    medicalHistoryArea.getText();

            String chronicMedication =
                    chronicMedicationArea.getText();

            String notes =
                    notesArea.getText();

            long newPatientId =
                    patientService.insertPatient(
                            firstname,
                            lastname,
                            phone,
                            amka,
                            smokingStatus,
                            height,
                            weight,
                            medicalHistory,
                            chronicMedication,
                            notes
                    );

            flushVaccines(newPatientId);

            DatabaseChangeTracker.markChanged();

            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);

            Stage stage =
                    (Stage) cancelButton.getScene().getWindow();

            stage.close();

        } catch (RuntimeException e) {

            formErrorLabel.setText(e.getMessage());
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }
    }


    private void flushVaccines(long patientId) {

        List<Vaccine> newVaccines = new ArrayList<>();

        for (Vaccine vaccine : vaccineBuffer.values()) {

            newVaccines.add(
                    new Vaccine(
                            vaccine.getName(),
                            -1,
                            vaccine.getDate(),
                            (int) patientId,
                            vaccine.getShotnumber()
                    )
            );
        }

        vaccineService.saveVaccineBuffer(
                newVaccines,
                new ArrayList<>()
        );
    }


    @FXML
    public void addVaccine() {

        try {

            String vaccineType =
                    vaccineTypeField.getText();

            Integer shotNumber =
                    NumberInputHelpers.parseInteger(
                            vaccineDoseField.getText(),
                            "Δόση"
                    );

            LocalDate vaccinationDate =
                    vaccineDatePicker.getValue();

            Vaccine vaccine =
                    new Vaccine(
                            vaccineType,
                            -1,
                            vaccinationDate,
                            -1,
                            shotNumber
                    );

            int pseudoId = vaccineBufferCounter++;

            vaccine.setPseudoId(pseudoId);

            vaccineBuffer.put(
                    pseudoId,
                    vaccine
            );

            vaccineTypeField.setText("");
            vaccineDoseField.setText("");
            vaccineDatePicker.setValue(null);

            loadVaccines();

            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);

        } catch (RuntimeException e) {

            formErrorLabel.setText(e.getMessage());
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }
    }


    private void initializeTableColumns() {

        vaccineTypeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().getName()
                        )
        );

        vaccineIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData.getValue().getPseudoId()
                        )
        );

        vaccineDoseColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData.getValue().getShotnumber()
                        )
        );

        vaccineDateColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                DateParser.getStringDate(
                                        cellData.getValue().getDate()
                                )
                        )
        );

        deleteVaccineColumn.setCellFactory(
                column -> new TableCell<>() {

                    private final Button delButton = new Button();

                    {
                        Image image =
                                new Image(
                                        Objects.requireNonNull(
                                                getClass().getResourceAsStream(
                                                        "/images/trash-solid.png"
                                                )
                                        )
                                );

                        ImageView imageView = new ImageView(image);

                        imageView.setFitWidth(14);
                        imageView.setFitHeight(14);
                        imageView.setPreserveRatio(true);

                        delButton.setPadding(
                                new javafx.geometry.Insets(0)
                        );

                        delButton.setStyle(
                                "-fx-background-radius: 5;"
                        );

                        delButton.setMinSize(30, 30);
                        delButton.setPrefSize(30, 30);

                        delButton.setGraphic(imageView);

                        delButton.setOnAction(event -> {

                            /*
                             * More reliable than getIndex() when
                             * TableView virtualizes/recycles cells.
                             */
                            Vaccine vaccine = getTableRow().getItem();

                            if (vaccine == null) {
                                return;
                            }

                            vaccineBuffer.remove(
                                    vaccine.getPseudoId()
                            );

                            loadVaccines();
                        });
                    }

                    @Override
                    protected void updateItem(
                            Void item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        setText(null);

                        if (empty || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(delButton);
                        }
                    }
                }
        );

        vaccineTypeColumn.setStyle(
                "-fx-alignment: CENTER;"
        );

        vaccineDateColumn.setStyle(
                "-fx-alignment: CENTER;"
        );

        vaccineDoseColumn.setStyle(
                "-fx-alignment: CENTER;"
        );

        vaccineIdColumn.setStyle(
                "-fx-alignment: CENTER;"
        );
    }


    private void loadVaccines() {

        /*
         * No clear()
         * No setItems()
         * No refresh()
         */
        vaccines.setAll(
                vaccineBuffer.values()
        );

        totalVaccinesLabel.setText(
                Integer.toString(vaccines.size())
        );
    }


    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }
}