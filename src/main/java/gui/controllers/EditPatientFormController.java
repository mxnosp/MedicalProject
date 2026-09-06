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
import java.util.List;
import java.util.Objects;

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

    private  VaccineService vaccineService;

    private final ObservableList<Vaccine> vaccines = FXCollections.observableArrayList();

    private final ArrayList<Vaccine> vaccineBuffer=new ArrayList<>();

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

    private List<Integer> delVaccines;
    private Integer vaccineBufferCounter;

    @FXML
    private void initialize(){
        delVaccines=new ArrayList<>();
        vaccineBuffer.clear();
        vaccineService=new VaccineService();
        patientService=new PatientService();
        initializeTableColumns();
    }

    /**
     * initializes the vaccine table's columns
     */
    private void initializeTableColumns() {
        vaccineTypeColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getName())
        );

        vaccineIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));


        vaccineDoseColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShotnumber()));

        vaccineDateColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(DateParser.getStringDate(cellData.getValue().getDate()))
        );

        deleteVaccineColumn.setCellFactory(column -> new TableCell<Vaccine, Void>() {

            private final Button delButton = new Button();

            {
                delButton.setOnAction(event -> {
                    Vaccine vaccine = getTableView().getItems().get(getIndex());
                    vaccineBuffer.remove(vaccine);
                    delVaccines.add(vaccine.getId());
                    loadVaccines(vaccineBuffer);
                });
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash-solid.png")));
                ImageView imageView = new ImageView(image);
                delButton.setPadding(new javafx.geometry.Insets(0));
                delButton.setStyle("-fx-background-radius: 5;");
                delButton.setMinSize(30, 30);
                delButton.setPrefSize(30, 30);
                imageView.setFitWidth(14);
                imageView.setFitHeight(14);
                imageView.setPreserveRatio(true);
                delButton.setGraphic(imageView);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(delButton);
                }
            }
        });
    }

    /**
     * loads the vaccines that exist on the buffer to the table
     */
    private void loadVaccines(List<Vaccine> activevaccines){
        vaccines.clear();
        vaccines.setAll(activevaccines);
        vaccinesTable.setItems(vaccines);
    }

    private void flushVaccines(long patient_id){
        for(Integer id : delVaccines){
            vaccineService.deleteVaccine(id);
        }
        for(Vaccine v : vaccineBuffer){
            List<Vaccine> currentVaccines=new ArrayList<>(vaccineService.getPatientVaccines(selectedPatient.getPatientId()));
            if(!currentVaccines.contains(v)){
                vaccineService.insertVaccine(v.getName(),v.getShotnumber(),v.getDate(), (int) patient_id);
            }
        }
    }

    /**
     * adds the vaccine to the vaccine buffer
     */
    public void addVaccine(){
        try{
            String vaccinetype=vaccineTypeField.getText();
            Integer shotnumber=NumberInputHelpers.parseInteger(vaccineDoseField.getText(),"Δόση");
            LocalDate vaccinationDate=vaccineDatePicker.getValue();
            vaccineBuffer.add(new Vaccine(vaccinetype, vaccineBufferCounter++,vaccinationDate,-1,shotnumber));
            vaccineTypeField.setText("");
            vaccineDoseField.setText("");
            vaccineDatePicker.setValue(null);
            loadVaccines(vaccineBuffer);
            formErrorLabel.setText("");
            formErrorLabel.setVisible(false);
            formErrorLabel.setManaged(false);
        }catch (RuntimeException e){
            formErrorLabel.setText(e.getMessage());
            formErrorLabel.setVisible(true);
            formErrorLabel.setManaged(true);
        }

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
            flushVaccines(selectedPatient.getPatientId());
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
        List<Vaccine> patientVaccines=vaccineService.getPatientVaccines(selectedPatient.getPatientId());
        vaccineBuffer.addAll(patientVaccines);
        loadVaccines(vaccineBuffer);
        vaccineBufferCounter =vaccineBuffer.size();
        totalVaccinesLabel.setText(vaccineBufferCounter.toString());

    }
}
