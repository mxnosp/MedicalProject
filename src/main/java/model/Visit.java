package model;

import model.exceptions.ValidationException;

/**
 * Class model.Visit represents a visit in the doctor's office
 * by a patient it includes the date if he has paid for the visit
 */

public class Visit {
    private  Date date;
    private boolean paid;
    private String notes;
    private final int patient_id;

    /**
     * model.Visit constructor initializes the date of the visit the paid boolean and the notes of the visit if the notes string is null that means that
     * no notes were given for that visit
     * @param day
     * @param month
     * @param year
     * @param notes
     * @param paid
     * @param patient_id
     * @throws ValidationException if the date constructor throws it
     */
    Visit(int day,int month ,int year,String notes,boolean paid,int patient_id) throws ValidationException{
        date=new Date(day,month,year);
        this.paid=paid;
        this.notes=notes;
        this.patient_id=patient_id;
    }

    /**
     * @return true if the visit was paid otherwise false
     */
    public boolean isPaidVisit(){
        return paid;
    }

    /**
     * @return the date of the visit
     */
    public Date getVisitDate(){
        return date;
    }

    /**
     * @return the notes of the visit
     */
    public String getVisitNotes(){
        return notes;
    }

    /**
     * @return the patient id of the patient that made the visit
     */
    public int getPatientid(){
        return patient_id;
    }
}
