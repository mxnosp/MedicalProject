package gui.controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Patient;
import model.Visit;
import service.PatientService;
import service.VisitService;
import utils.DateParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PatientDashboardController {

    private Patient selectedPatient = null;
    private Visit selectedVisit = null;

    private PatientService patientService;
    private VisitService visitService;


    @FXML
    private Label searchErrorLabel;

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
    private VBox selectedPatientPanel;

    @FXML
    private VBox visitsPanel;

    @FXML
    private Button addPatient;

    @FXML
    private Button editPatient;

    @FXML
    private Button deletePatient;

    private final Map<Node, Animation> activeAnimations =
            new HashMap<>();


    // =========================
    // Patient Table
    // =========================

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


    // =========================
    // Visit Table
    // =========================

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


    private final ObservableList<Patient> patients =
            FXCollections.observableArrayList();

    private final ObservableList<Visit> visits =
            FXCollections.observableArrayList();


    public PatientDashboardController() {

        patientService = new PatientService();
        visitService = new VisitService();
    }


    /**
     * Initializes the dashboard tables.
     */
    @FXML
    private void initialize() {

        patientTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        visitTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        initializeTableColumns();

        configurePatientTable();
        configureVisitTable();

        loadPatients();

        initializePatientSelection();
        initializeVisitSelection();

        setInitialVisibilityState();
        updateVisitActionState();
    }


    /**
     * Configures the patient table.
     *
     * Columns:
     * - can be resized
     * - cannot be sorted
     * - cannot be reordered
     */
    private void configurePatientTable() {

        // Bind ObservableList only once
        patientTable.setItems(patients);

        // Disable sorting
        idColumn.setSortable(false);
        firstNameColumn.setSortable(false);
        lastNameColumn.setSortable(false);
        amkaColumn.setSortable(false);

        // Disable column reordering
        idColumn.setReorderable(false);
        firstNameColumn.setReorderable(false);
        lastNameColumn.setReorderable(false);
        amkaColumn.setReorderable(false);

        // Allow resizing
        idColumn.setResizable(true);
        firstNameColumn.setResizable(true);
        lastNameColumn.setResizable(true);
        amkaColumn.setResizable(true);

        // Completely disable sorting
        patientTable.getSortOrder().clear();
        patientTable.setSortPolicy(table -> false);

        // Center cells
        idColumn.setStyle("-fx-alignment: CENTER;");
        firstNameColumn.setStyle("-fx-alignment: CENTER;");
        lastNameColumn.setStyle("-fx-alignment: CENTER;");
        amkaColumn.setStyle("-fx-alignment: CENTER;");
    }


    /**
     * Configures the visit table.
     *
     * Columns:
     * - can be resized
     * - cannot be sorted
     * - cannot be reordered
     */
    private void configureVisitTable() {

        // Bind ObservableList only once
        visitTable.setItems(visits);

        // Disable sorting
        visitIdColumn.setSortable(false);
        visitDateColumn.setSortable(false);
        viewVisitColumn.setSortable(false);

        // Disable column reordering
        visitIdColumn.setReorderable(false);
        visitDateColumn.setReorderable(false);
        viewVisitColumn.setReorderable(false);

        // Allow resizing
        visitIdColumn.setResizable(true);
        visitDateColumn.setResizable(true);
        viewVisitColumn.setResizable(true);

        // Completely disable sorting
        visitTable.getSortOrder().clear();
        visitTable.setSortPolicy(table -> false);

        // Center cells
        visitIdColumn.setStyle("-fx-alignment: CENTER;");
        visitDateColumn.setStyle("-fx-alignment: CENTER;");
        viewVisitColumn.setStyle("-fx-alignment: CENTER;");
    }


    /**
     * Sets the action performed when
     * a patient row is selected.
     */
    private void initializePatientSelection() {

        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldPatient, newPatient) -> {

                            if (newPatient == null) {

                                clearSelectedPatientLabels();
                                clearVisits();
                                hidePatientDependentUiAnimated();

                                return;
                            }

                            showSelectedPatient(newPatient);
                            loadVisits();
                            showPatientDependentUiAnimated();
                        }
                );
    }


    /**
     * Sets the action performed when
     * a visit row is selected.
     */
    private void initializeVisitSelection() {

        visitTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldVisit, newVisit) -> {

                            selectedVisit = newVisit;
                            updateVisitActionState();
                        }
                );
    }


    /**
     * Keeps Add Patient available at all times.
     * Everything that requires a selected patient starts hidden.
     */
    private void setInitialVisibilityState() {

        hidePanelImmediately(selectedPatientPanel);
        hidePanelImmediately(visitsPanel);

        hideButtonImmediately(editPatient);
        hideButtonImmediately(deletePatient);
    }


    /**
     * Shows the patient-dependent sections with a short fade/slide animation.
     */
    private void showPatientDependentUiAnimated() {

        animatePanelIn(selectedPatientPanel);
        animatePanelIn(visitsPanel);

        animateButtonIn(editPatient);
        animateButtonIn(deletePatient);

        updateVisitActionState();
    }


    /**
     * Hides the patient-dependent sections while keeping the dashboard layout stable.
     */
    private void hidePatientDependentUiAnimated() {

        animatePanelOut(selectedPatientPanel);
        animatePanelOut(visitsPanel);

        animateButtonOut(editPatient);
        animateButtonOut(deletePatient);

        updateVisitActionState();
    }


    /**
     * Edit/Delete Visit are only usable when a visit is selected.
     */
    private void updateVisitActionState() {

        boolean noVisitSelected = selectedVisit == null;

        editVisit.setDisable(noVisitSelected);
        deleteVisit.setDisable(noVisitSelected);
    }


    private void hidePanelImmediately(Node node) {

        stopActiveAnimation(node);

        node.setOpacity(0);
        node.setTranslateY(0);
        node.setVisible(false);
        node.setManaged(true);
        node.setMouseTransparent(true);
    }


    private void hideButtonImmediately(Button button) {

        stopActiveAnimation(button);

        button.setOpacity(0);
        button.setTranslateY(0);
        button.setVisible(false);
        button.setManaged(false);
        button.setDisable(true);
    }


    private void animatePanelIn(Node node) {

        stopActiveAnimation(node);

        node.setManaged(true);
        node.setVisible(true);
        node.setMouseTransparent(false);

        node.setOpacity(0);
        node.setTranslateY(8);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(180),
                        node
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(180),
                        node
                );

        slide.setFromY(8);
        slide.setToY(0);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        slide
                );

        playAnimation(node, animation, null);
    }


    private void animatePanelOut(Node node) {

        stopActiveAnimation(node);

        node.setMouseTransparent(true);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(120),
                        node
                );

        fade.setFromValue(node.getOpacity());
        fade.setToValue(0);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(120),
                        node
                );

        slide.setFromY(node.getTranslateY());
        slide.setToY(4);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        slide
                );

        playAnimation(
                node,
                animation,
                () -> {
                    node.setVisible(false);
                    node.setTranslateY(0);
                }
        );
    }


    private void animateButtonIn(Button button) {

        stopActiveAnimation(button);

        button.setManaged(true);
        button.setVisible(true);
        button.setDisable(false);
        button.setMouseTransparent(false);

        button.setOpacity(0);
        button.setTranslateY(6);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(160),
                        button
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(160),
                        button
                );

        slide.setFromY(6);
        slide.setToY(0);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        slide
                );

        playAnimation(button, animation, null);
    }


    private void animateButtonOut(Button button) {

        stopActiveAnimation(button);

        button.setDisable(true);
        button.setMouseTransparent(true);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(100),
                        button
                );

        fade.setFromValue(button.getOpacity());
        fade.setToValue(0);

        TranslateTransition slide =
                new TranslateTransition(
                        Duration.millis(100),
                        button
                );

        slide.setFromY(button.getTranslateY());
        slide.setToY(4);

        ParallelTransition animation =
                new ParallelTransition(
                        fade,
                        slide
                );

        playAnimation(
                button,
                animation,
                () -> {
                    button.setVisible(false);
                    button.setManaged(false);
                    button.setTranslateY(0);
                }
        );
    }


    private void playAnimation(
            Node node,
            Animation animation,
            Runnable onFinished
    ) {

        activeAnimations.put(
                node,
                animation
        );

        animation.setOnFinished(event -> {

            if (activeAnimations.get(node) == animation) {
                activeAnimations.remove(node);
            }

            if (onFinished != null) {
                onFinished.run();
            }
        });

        animation.play();
    }


    private void stopActiveAnimation(Node node) {

        Animation animation =
                activeAnimations.remove(node);

        if (animation != null) {
            animation.stop();
        }
    }


    /**
     * Clears the selected patient's
     * information from the side panel.
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
     * Displays information about
     * the selected patient.
     *
     * @param patientToShow patient to display
     */
    private void showSelectedPatient(Patient patientToShow) {

        clearSelectedPatientLabels();

        selectedPatientIdLabel.setText(
                String.valueOf(
                        patientToShow.getPatientId()
                )
        );

        selectedAmkaLabel.setText(
                patientToShow.getPatientAmka()
        );

        if (patientToShow.getPatientPhone() != null
                && !patientToShow.getPatientPhone().isEmpty()) {

            selectedPhoneLabel.setText(
                    patientToShow.getPatientPhone()
            );
        }

        selectedFirstNameLabel.setText(
                patientToShow.getPatientFirstName()
        );

        selectedLastNameLabel.setText(
                patientToShow.getPatientLastName()
        );

        if (patientToShow.getPatientBMI() != null) {

            selectedBMILabel.setText(
                    Double.toString(
                            patientToShow.getPatientBMI()
                    )
            );
        }

        if (patientToShow.getPatientSmokingStatus() != null) {

            selectedSmokerLabel.setText(
                    patientToShow
                            .getPatientSmokingStatus()
                            .toString()
            );
        }

        if (patientToShow.getPatientWeight() != null) {

            selectedWeightLabel.setText(
                    patientToShow.getPatientWeight()
                            + "kg"
            );
        }

        if (patientToShow.getPatientHeight() != null) {

            selectedHeightLabel.setText(
                    patientToShow.getPatientHeight()
                            + "cm"
            );
        }

        selectedPatient =
                new Patient(
                        patientToShow.getPatientId(),
                        patientToShow.getPatientFirstName(),
                        patientToShow.getPatientLastName(),
                        patientToShow.getPatientPhone(),
                        patientToShow.getPatientAmka(),

                        patientToShow.getPatientSmokingStatus() == null
                                ? null
                                : patientToShow
                                .getPatientSmokingStatus()
                                .ordinal(),

                        patientToShow.getPatientHeight(),
                        patientToShow.getPatientWeight(),
                        patientToShow.getPatientMedicalHistory(),
                        patientToShow.getPatientChronicMedication(),
                        patientToShow.getPatientNotes()
                );
    }


    /**
     * Initializes the patient
     * and visit table columns.
     */
    private void initializeTableColumns() {

        // =========================
        // Patient columns
        // =========================

        idColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData
                                        .getValue()
                                        .getPatientId()
                        )
        );

        firstNameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientFirstName()
                        )
        );

        lastNameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientLastName()
                        )
        );

        amkaColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientAmka()
                        )
        );


        // =========================
        // Visit columns
        // =========================

        visitIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData
                                        .getValue()
                                        .getId()
                        ).asObject()
        );

        visitDateColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                DateParser.getStringDate(
                                        cellData
                                                .getValue()
                                                .getVisitDate()
                                )
                        )
        );


        /*
         * View visit button column.
         */
        viewVisitColumn.setCellFactory(
                column ->
                        new TableCell<Visit, Void>() {

                            private final Button viewButton =
                                    new Button();

                            {
                                Image image =
                                        new Image(
                                                Objects.requireNonNull(
                                                        getClass()
                                                                .getResourceAsStream(
                                                                        "/images/view.png"
                                                                )
                                                )
                                        );

                                ImageView imageView =
                                        new ImageView(image);

                                imageView.setFitWidth(18);
                                imageView.setFitHeight(18);
                                imageView.setPreserveRatio(true);

                                viewButton.setGraphic(
                                        imageView
                                );

                                viewButton.setOnAction(
                                        event -> {

                                            /*
                                             * Using the TableRow item is safer
                                             * with JavaFX cell virtualization.
                                             */
                                            Visit visit =
                                                    getTableRow()
                                                            .getItem();

                                            if (visit == null) {
                                                return;
                                            }

                                            viewSelectedVisit(
                                                    visit
                                            );
                                        }
                                );
                            }

                            @Override
                            protected void updateItem(
                                    Void item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                setText(null);

                                if (empty
                                        || getTableRow()
                                        .getItem() == null) {

                                    setGraphic(null);

                                } else {

                                    setGraphic(
                                            viewButton
                                    );
                                }
                            }
                        }
        );
    }


    /**
     * Loads all patients into
     * the ObservableList.
     */
    private void loadPatients() {

        patients.setAll(
                patientService.getAllPatients()
        );
    }


    /**
     * Searches the patient table.
     */
    @FXML
    void searchPatient() {

        Patient searchedByAmka =
                patientService.searchPatientByAmka(
                        searchField.getText()
                );

        if (searchedByAmka != null) {

            patients.setAll(
                    searchedByAmka
            );

        } else {

            List<Patient> results =
                    patientService.searchPatientsByName(
                            searchField.getText()
                    );

            patients.setAll(
                    results
            );
        }

        /*
         * Search may change/clear the
         * current table selection.
         */
        if (selectedPatient == null) {
            clearVisits();
        }
    }


    /**
     * Called when the more info
     * button is pressed.
     */
    @FXML
    void showFUllPatientInfo(ActionEvent event) {

        if (selectedPatient == null) {
            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/viewpatientscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            ViewPatientController controller =
                    loader.getController();

            controller.setSelectedPatient(
                    selectedPatient
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage patientStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            patientStage
                    .getIcons()
                    .add(icon);

            patientStage.setTitle(
                    "Κάρτα Ασθενή"
            );

            patientStage.setScene(
                    scene
            );

            patientStage.setResizable(
                    false
            );

            Stage ownerStage =
                    (Stage) ((Node) event
                            .getSource())
                            .getScene()
                            .getWindow();

            patientStage.initOwner(
                    ownerStage
            );

            patientStage.initModality(
                    Modality.WINDOW_MODAL
            );

            patientStage.showAndWait();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load patient form",
                    e
            );
        }
    }


    /**
     * Opens the add patient form.
     */
    @FXML
    private void openNewPatientForm(
            ActionEvent event
    ) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/addpatientscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            AddPatientFormController controller =
                    loader.getController();

            controller.setSelectedPatient(
                    selectedPatient
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage patientStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            patientStage
                    .getIcons()
                    .add(icon);

            patientStage.setTitle(
                    "Κάρτα Ασθενή"
            );

            patientStage.setScene(
                    scene
            );

            patientStage.setResizable(
                    false
            );

            Stage ownerStage =
                    (Stage) ((Node) event
                            .getSource())
                            .getScene()
                            .getWindow();

            patientStage.initOwner(
                    ownerStage
            );

            patientStage.initModality(
                    Modality.WINDOW_MODAL
            );

            patientStage.showAndWait();

            loadPatients();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load patient form",
                    e
            );
        }
    }


    /**
     * Deletes the selected patient
     * and their visits.
     */
    @FXML
    void deleteSelectedPatient() {

        if (selectedPatient == null) {
            return;
        }

        int patientId =
                selectedPatient.getPatientId();

        patientService.deletePatient(
                patientId
        );

        visitService.deletePatientVisits(
                patientId
        );

        clearSelectedPatientLabels();
        clearVisits();
        hidePatientDependentUiAnimated();

        loadPatients();

        selectedPatient = null;
    }


    /**
     * Clears the visits table.
     */
    private void clearVisits() {

        visits.clear();
        selectedVisit = null;
        updateVisitActionState();
    }


    /**
     * Loads the visits for
     * the currently selected patient.
     */
    private void loadVisits() {

        selectedVisit = null;
        updateVisitActionState();

        if (selectedPatient == null) {

            visits.clear();

            return;
        }

        visits.setAll(
                visitService.getPatientVisits(
                        selectedPatient
                                .getPatientId()
                )
        );
    }


    /**
     * Opens the selected patient's
     * edit form.
     */
    @FXML
    void openSelectedPatientForm(
            ActionEvent event
    ) {

        if (selectedPatient == null) {
            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/editpatientscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            EditPatientFormController controller =
                    loader.getController();

            controller.setSelectedPatient(
                    selectedPatient
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage patientStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            patientStage
                    .getIcons()
                    .add(icon);

            patientStage.setTitle(
                    "Επεξεργασία Ασθενή"
            );

            patientStage.setScene(
                    scene
            );

            patientStage.setResizable(
                    false
            );

            Stage ownerStage =
                    (Stage) ((Node) event
                            .getSource())
                            .getScene()
                            .getWindow();

            patientStage.initOwner(
                    ownerStage
            );

            patientStage.initModality(
                    Modality.WINDOW_MODAL
            );

            patientStage.showAndWait();

            loadPatients();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load patient form",
                    e
            );
        }
    }


    /**
     * Opens the add visit form.
     */
    @FXML
    private void openNewVisitForm(
            ActionEvent event
    ) {

        if (selectedPatient == null) {
            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/addvisitscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            AddVisitFormController controller =
                    loader.getController();

            controller.setSelectedPatient(
                    selectedPatient
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage visitStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            visitStage
                    .getIcons()
                    .add(icon);

            visitStage.setTitle(
                    "Προσθήκη Επίσκεψης"
            );

            visitStage.setScene(
                    scene
            );

            Stage ownerStage =
                    (Stage) ((Node) event
                            .getSource())
                            .getScene()
                            .getWindow();

            visitStage.initOwner(
                    ownerStage
            );

            visitStage.initModality(
                    Modality.WINDOW_MODAL
            );

            visitStage.showAndWait();

            loadVisits();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load visit form",
                    e
            );
        }
    }


    /**
     * Opens the selected visit
     * edit form.
     */
    @FXML
    private void openSelectedVisitForm(
            ActionEvent event
    ) {

        if (selectedVisit == null) {
            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/editvisitscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            EditVisitFormController controller =
                    loader.getController();

            controller.setSelectedVisit(
                    selectedVisit
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage visitStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            visitStage
                    .getIcons()
                    .add(icon);

            visitStage.setTitle(
                    "Επεξεργασία Επίσκεψης"
            );

            visitStage.setScene(
                    scene
            );

            Stage ownerStage =
                    (Stage) ((Node) event
                            .getSource())
                            .getScene()
                            .getWindow();

            visitStage.initOwner(
                    ownerStage
            );

            visitStage.initModality(
                    Modality.WINDOW_MODAL
            );

            visitStage.showAndWait();

            selectedVisit = null;

            loadVisits();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load visit form",
                    e
            );
        }
    }


    /**
     * Deletes the selected visit.
     */
    @FXML
    private void deleteSelectedVisit() {

        if (selectedVisit == null) {
            return;
        }

        visitService.deleteVisit(
                selectedVisit.getId()
        );

        selectedVisit = null;

        loadVisits();
    }


    /**
     * Opens the selected visit
     * in read-only mode.
     *
     * @param visit visit to display
     */
    private void viewSelectedVisit(
            Visit visit
    ) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/viewvisitscreen.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            ViewVisitController controller =
                    loader.getController();

            controller.setVisit(
                    visit
            );

            Scene scene =
                    new Scene(
                            root,
                            700,
                            950
                    );

            Stage viewVisitStage =
                    new Stage();

            Image icon =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/images/doctorlogo.png"
                                    )
                    );

            viewVisitStage
                    .getIcons()
                    .add(icon);

            viewVisitStage.setTitle(
                    "Προβολή Επίσκεψης"
            );

            viewVisitStage.setScene(
                    scene
            );

            Stage ownerStage =
                    (Stage) visitTable
                            .getScene()
                            .getWindow();

            viewVisitStage.initOwner(
                    ownerStage
            );

            viewVisitStage.initModality(
                    Modality.WINDOW_MODAL
            );

            viewVisitStage.showAndWait();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load view visit form",
                    e
            );
        }
    }
}