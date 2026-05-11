package model;

import model.exceptions.ValidationException;

import java.time.LocalDate;

/**
 * Class model.Visit represents a visit in the doctor's office
 * by a patient it includes the date if he has paid for the visit
 */

public class Visit {
    private final Integer id;
    private final LocalDate date;
    private final Integer payment;
    private final Spirometry spirometry;
    private final Integer heartRate;
    private final Integer spo2;
    private final String physicalCheck;
    private final String functionalCheck;
    private final String notes;
    private final String medication;
    private final String reason;
    private final LocalDate reappoinment;
    private final int patient_id;

    /**
     * model.Visit constructor initializes the date of the visit the paid boolean and the notes of the visit if the notes string is null that means that
     * no notes were given for that visit
     * @param date
     * @param spo2
     * @param reason
     * @param functionalCheck
     * @param heartRate
     * @param id
     * @param medication
     * @param notes
     * @param payment
     * @param patient_id
     * @throws ValidationException if the date constructor throws it
     */
    public Visit(int id, String notes, Integer payment, LocalDate date, int patient_id, Spirometry spirometry, Integer heartRate, Integer spo2, String physicalCheck, String functionalCheck, String medication, String reason,LocalDate reappoinment) throws ValidationException{
        this.id=id;
        this.medication = medication;
        this.reason = reason;
        if(date==null){
            throw new ValidationException("Visit's date is mandatory!");
        }
        this.date=date;
        this.reappoinment=reappoinment;
        if(payment!=null){
            if(payment<0){
                throw new ValidationException("Λάθος Χρέωση!");
            }
        }
        this.payment=payment;
        this.notes=notes;
        this.patient_id=patient_id;
        this.spirometry=spirometry;
        if(heartRate!=null){
            if(heartRate<=0){
                throw new ValidationException("Λάθος Σφύξεις!");
            }
        }
        this.heartRate=heartRate;
        if(spo2!=null){
            if(spo2<=0){
                throw new ValidationException("Λάθος SpO₂!");
            }
        }
        this.spo2=spo2;
        this.physicalCheck=physicalCheck;
        this.functionalCheck=functionalCheck;
    }

    /**
     * @return the visit's payment
     */
    public Integer getVisitPayment(){
        return payment;
    }

    /**
     * @return the date of the visit
     */
    public LocalDate getVisitDate(){
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

    /**
     * @return the id of the visit
     */
    public int getId(){return id;}

    public Spirometry getSpirometry() {
        return spirometry;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public Integer getSpo2() {
        return spo2;
    }

    public String getPhysicalCheck() {
        return physicalCheck;
    }

    public String getFunctionalCheck() {
        return functionalCheck;
    }

    public String getMedication() {
        return medication;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getReappoinment() {
        return reappoinment;
    }
}
