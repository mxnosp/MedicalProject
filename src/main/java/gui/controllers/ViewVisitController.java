package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Visit;
import utils.DateParser;
import utils.NumberInputHelpers;

public class   ViewVisitController {
    @FXML private Label visitDateLabel;
    @FXML private Label reasonLabel;
    @FXML private Label paymentLabel;

    @FXML private Label spo2Label;
    @FXML private Label pulseLabel;
    @FXML private Label fev1Label;
    @FXML private Label fvcLabel;
    @FXML private Label pefLabel;
    @FXML private Label fev1FvcLabel;
    @FXML private Label fef2575Label;

    @FXML private Label physicalExamLabel;
    @FXML private Label functionalExamLabel;
    @FXML private Label treatmentLabel;
    @FXML private Label notesLabel;

    @FXML private Label recheckDateLabel;

    @FXML private Button closeButton;
    private Visit visit;

    @FXML
    private void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    void setVisit(Visit visit){
        this.visit=visit;
        setFields();
    }

    private void setFields(){
        visitDateLabel.setText(DateParser.getStringDate(visit.getVisitDate())==null?"":DateParser.getStringDate(visit.getVisitDate()));
        reasonLabel.setText(visit.getReason());
        paymentLabel.setText(NumberInputHelpers.StringFromInteger(visit.getVisitPayment())+"€");
        spo2Label.setText(NumberInputHelpers.StringFromInteger(visit.getSpo2()));
        pulseLabel.setText(NumberInputHelpers.StringFromInteger(visit.getHeartRate()));
        fev1Label.setText(NumberInputHelpers.StringFromDouble(visit.getSpirometry().getFEV1()));
        fvcLabel.setText(NumberInputHelpers.StringFromDouble(visit.getSpirometry().getFVC()));
        pefLabel.setText(NumberInputHelpers.StringFromDouble(visit.getSpirometry().getPEF()));
        fef2575Label.setText(NumberInputHelpers.StringFromDouble(visit.getSpirometry().getFEF2575()));
        if(visit.getSpirometry().getFEV1()!=null && visit.getSpirometry().getFVC()!=null){
            double division=visit.getSpirometry().getFEV1()/visit.getSpirometry().getFVC();
            fev1FvcLabel.setText(NumberInputHelpers.StringFromDouble(Math.round(division*100.0)/100.0));
        };
        physicalExamLabel.setText(visit.getPhysicalCheck());
        functionalExamLabel.setText(visit.getFunctionalCheck());
        treatmentLabel.setText(visit.getMedication());
        notesLabel.setText(visit.getVisitNotes());
        recheckDateLabel.setText(DateParser.getStringDate(visit.getReappoinment())==null?"":DateParser.getStringDate(visit.getReappoinment()));



    }

}
