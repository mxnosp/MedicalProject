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
import utils.*;

import java.time.LocalDate;
import java.util.*;

public class EditPatientFormController {

    private static final double VACCINE_TABLE_HEIGHT = 150;

    private Patient selectedPatient;

    private PatientService patientService;
    private VaccineService vaccineService;

    private final ObservableList<Vaccine> vaccines =
            FXCollections.observableArrayList();

    // pseudoId -> VaccineTracker
    private final Map<Integer, VaccineTracker> vaccineBuffer =
            new LinkedHashMap<>();

    private int currentPseudoId;


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
    private TextField vaccineTypeField;

    @FXML
    private DatePicker vaccineDatePicker;

    @FXML
    private TextField vaccineDoseField;

    @FXML
    private Button addVaccineButton;

    @FXML
    private Label totalVaccinesLabel;


    @FXML
    private void initialize() {

        vaccineService = new VaccineService();
        patientService = new PatientService();

        initializeTableColumns();
        configureVaccineTable();
    }


    private void configureVaccineTable() {

        // Set the list only once
        vaccinesTable.setItems(vaccines);

        // Same exact height as AddPatient
        vaccinesTable.setMinHeight(VACCINE_TABLE_HEIGHT);
        vaccinesTable.setPrefHeight(VACCINE_TABLE_HEIGHT);
        vaccinesTable.setMaxHeight(VACCINE_TABLE_HEIGHT);

        // Disable sorting
        vaccineIdColumn.setSortable(false);
        vaccineTypeColumn.setSortable(false);
        vaccineDateColumn.setSortable(false);
        vaccineDoseColumn.setSortable(false);
        deleteVaccineColumn.setSortable(false);

        // Disable column dragging
        vaccineIdColumn.setReorderable(false);
        vaccineTypeColumn.setReorderable(false);
        vaccineDateColumn.setReorderable(false);
        vaccineDoseColumn.setReorderable(false);
        deleteVaccineColumn.setReorderable(false);

        vaccinesTable.getSortOrder().clear();
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

                            Vaccine vaccine =
                                    getTableRow().getItem();

                            if (vaccine == null) {
                                return;
                            }

                            VaccineTracker tracker =
                                    vaccineBuffer.get(
                                            vaccine.getPseudoId()
                                    );

                            if (tracker == null) {
                                return;
                            }

                            if (tracker.getVaccineState()
                                    == VaccineState.NEW) {

                                vaccineBuffer.remove(
                                        vaccine.getPseudoId()
                                );

                            } else {

                                tracker.setVaccineState(
                                        VaccineState.DELETED
                                );
                            }

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

        List<Vaccine> activeVaccines =
                new ArrayList<>();

        for (VaccineTracker tracker : vaccineBuffer.values()) {

            if (tracker.getVaccineState()
                    != VaccineState.DELETED) {

                activeVaccines.add(
                        tracker.getVaccine()
                );
            }
        }

        /*
         * Only modify contents of ObservableList.
         * Do NOT call setItems() again.
         */
        vaccines.setAll(activeVaccines);

        totalVaccinesLabel.setText(
                Integer.toString(activeVaccines.size())
        );
    }


    private void flushVaccines() {

        List<Vaccine> deletedVaccines =
                new ArrayList<>();

        List<Vaccine> newVaccines =
                new ArrayList<>();

        for (VaccineTracker tracker : vaccineBuffer.values()) {

            if (tracker.getVaccineState()
                    == VaccineState.DELETED) {

                deletedVaccines.add(
                        tracker.getVaccine()
                );

            } else if (tracker.getVaccineState()
                    == VaccineState.NEW) {

                newVaccines.add(
                        tracker.getVaccine()
                );
            }
        }

        vaccineService.saveVaccineBuffer(
                newVaccines,
                deletedVaccines
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
                            selectedPatient.getPatientId(),
                            shotNumber
                    );

            int pseudoId = currentPseudoId++;

            vaccine.setPseudoId(pseudoId);

            vaccineBuffer.put(
                    pseudoId,
                    new VaccineTracker(
                            vaccine,
                            VaccineState.NEW
                    )
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


    @FXML
    private void cancelForm() {

        Stage stage =
                (Stage) cancelButton.getScene().getWindow();

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

            firstNameField
                    .getStyleClass()
                    .add("input-error");

            allfilled = false;

        } else {

            firstNameField
                    .getStyleClass()
                    .remove("input-error");
        }

        if (lastNameField.getText().isEmpty()) {

            lastNameField
                    .getStyleClass()
                    .add("input-error");

            allfilled = false;

        } else {

            lastNameField
                    .getStyleClass()
                    .remove("input-error");
        }

        if (amkaField.getText().isEmpty()) {

            amkaField
                    .getStyleClass()
                    .add("input-error");

            allfilled = false;

        } else {

            amkaField
                    .getStyleClass()
                    .remove("input-error");
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

            String firstname =
                    firstNameField.getText();

            String lastname =
                    lastNameField.getText();

            String amka =
                    amkaField.getText();

            String phone =
                    phoneField.getText();

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

            flushVaccines();

            patientService.updatePatient(
                    firstname,
                    lastname,
                    phone,
                    amka,
                    smokingStatus,
                    height,
                    weight,
                    medicalHistory,
                    chronicMedication,
                    notes,
                    selectedPatient.getPatientId()
            );

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


    public void setSelectedPatient(Patient selectedPatient) {

        this.selectedPatient = selectedPatient;

        smokingComboBox
                .getItems()
                .setAll(SmokingStatus.values());

        lastNameField.setText(
                selectedPatient.getPatientLastName()
        );

        firstNameField.setText(
                selectedPatient.getPatientFirstName()
        );

        phoneField.setText(
                selectedPatient.getPatientPhone()
        );

        amkaField.setText(
                selectedPatient.getPatientAmka()
        );

        smokingComboBox.setValue(
                selectedPatient.getPatientSmokingStatus()
        );

        heightField.setText(
                NumberInputHelpers.StringFromInteger(
                        selectedPatient.getPatientHeight()
                )
        );

        weightField.setText(
                NumberInputHelpers.StringFromInteger(
                        selectedPatient.getPatientWeight()
                )
        );

        bmiField.setText(
                NumberInputHelpers.StringFromDouble(
                        selectedPatient.getPatientBMI()
                )
        );

        medicalHistoryArea.setText(
                selectedPatient.getPatientMedicalHistory()
        );

        chronicMedicationArea.setText(
                selectedPatient.getPatientChronicMedication()
        );

        notesArea.setText(
                selectedPatient.getPatientNotes()
        );

        List<Vaccine> patientVaccines =
                vaccineService.getPatientVaccines(
                        selectedPatient.getPatientId()
                );

        for (Vaccine vaccine : patientVaccines) {

            int pseudoId = currentPseudoId++;

            vaccine.setPseudoId(pseudoId);

            vaccineBuffer.put(
                    pseudoId,
                    new VaccineTracker(
                            vaccine,
                            VaccineState.OLD
                    )
            );
        }

        loadVaccines();
    }
}