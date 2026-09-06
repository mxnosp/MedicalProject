package model;

import model.exceptions.ValidationException;

import java.time.LocalDate;

/**
 * Vaccine class represents a vaccine that a patient has done it stores the date he took the shot and
 * which shot it was ( first,second etc) and the name of the vaccine
 */
public class Vaccine{
    private final Integer id;
    private final LocalDate date;
    private final Integer patient_id;
    private final Integer shotnumber;
    private final String name;

    public Vaccine(String name,Integer id,LocalDate date,Integer patient_id,Integer shotnumber){
        this.id=id;
        this.patient_id=patient_id;
        if(name==null|| name.isBlank()){
            throw new ValidationException("Το όνομα του εμβολίου είναι υποχρεωτικό!");
        }
        this.name=name;
        if(date==null){
            throw new ValidationException("Η ημερομηνία εμβολίου είναι υποχρεωτική!");
        }
        this.date=date;
        if(shotnumber==null ){
            throw new ValidationException("Ο αριθμός δόσης του εμβολίου είναι υποχρεωτικος!");
        }
        this.shotnumber=shotnumber;
    }

    /**
     * returns the id of the vaccine
     * @return
     */
    public Integer getId(){
        return id;
    }

    /**
     * returns the id of the patient that took the vaccine
     * @return
     */
    public Integer getPatientId(){
        return patient_id;
    }

    /**
     * returns the date that the patient took the shot
     * @return
     */
    public LocalDate getDate(){
        return date;
    }

    /**
     * returns which number the shot was (first second etc)
     * @return
     */
    public Integer getShotnumber(){
        return shotnumber;
    }

    /**
     * returns the name of the vaccine
     * @return
     */
    public String getName(){
        return name;
    }
}