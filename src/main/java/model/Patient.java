package model;

import model.exceptions.InvalidAmkaException;
import model.exceptions.InvalidNameException;
import model.exceptions.InvalidPhoneException;
import model.exceptions.ValidationException;


/**
 * model.Patient class represents a patient and includes all his personal information and their visits to the
 * doc's office
 */
public class Patient {
    // Allows letters, spaces, hyphens and apostrophes in Greek or Latin names
    private static final String NAME_PATTERN = "[\\p{L}\\p{M}]+(?:[ '\\-’][\\p{L}\\p{M}]+)*";

    private final String firstname;
    private final String lastname;
    private final String amka;
    private final String  phone;
    private final int id;
    private final SmokingStatus smokingStatus;
    private final Integer height;
    private final Integer weight;
    private final String medicalHistory;
    private final String chronicMedication;
    private final String notes;

    /**
     * model.Patient constructor initializes the patient object
     * it checks that a valid firstname,lastname,amka and phone
     * is given by the user otherwise throws ValidationException
     * it also initializes the smoking status,
     * height,weight,medicalHistory,chronic medication and notes fields
     * @throws InvalidNameException if the first or last name is not valid
     * @throws InvalidAmkaException if the amka is not a 10-digit number
     * @throws InvalidPhoneException if the phone is not a 10=digit number
     * @param firstname
     * @param lastname
     * @param amka
     * @param phone
     * @param smokingStatus
     * @param chronicMedication
     * @param height
     * @param medicalHistory
     * @param weight
     * @param notes
     * @param id
     */
    public Patient(int id, String firstname, String lastname, String phone, String amka,Integer smokingStatus,Integer height,Integer weight,String medicalHistory,String chronicMedication,String notes) throws ValidationException {
        // Validates the fields that are required to create a patient
        if(firstname==null || !firstname.trim().matches(NAME_PATTERN)) {
            throw new InvalidNameException("Μη έγκυρο όνομα! Χρησιμοποιήστε μόνο γράμματα.");
        }
        if(lastname==null || !lastname.trim().matches(NAME_PATTERN)) {
            throw new InvalidNameException("Μη έγκυρο επώνυμο! Χρησιμοποιήστε μόνο γράμματα.");
        }
        if(amka==null||!amka.matches("\\d{11}")){
            throw new InvalidAmkaException("Μη έγκυρος ΑΜΚΑ! Ο ΑΜΚΑ πρέπει να αποτελείται από 11 ψηφία.");
        }
        if(phone!=null){
            if(!phone.isEmpty()){
                if(!phone.matches("\\d{10}")){
                    throw new InvalidPhoneException("Μη έγκυρος αριθμός τηλεφώνου! Το τηλέφωνο πρέπει να αποτελείται από 10 ψηφία.");
                }
            }
        }
        this.firstname=firstname;
        this.lastname=lastname;
        this.phone=phone;
        this.amka=amka;
        this.id=id;
        if(smokingStatus!=null)this.smokingStatus=SmokingStatus.values()[smokingStatus];
        else this.smokingStatus=null;
        this.height=height;
        this.weight=weight;
        this.medicalHistory=medicalHistory;
        this.chronicMedication=chronicMedication;
        this.notes=notes;
    }
    /**
     * @return the patient's first name
     */
    public String getPatientFirstName(){
        return firstname;
    }

    /**
     * @return the patient last name
     */
    public String getPatientLastName(){
        return lastname;
    }

    /**
     * @return the patient phone number
     */
    public String getPatientPhone(){
        return phone;
    }

    /**
     * @return the patient's amka
     */
    public String getPatientAmka(){
        return amka ;
    }

    /**
     * @return the patient's id
     */
    public Integer  getPatientId(){return id;}
    /**
     * @return the patient's smoking status
     */
    public SmokingStatus getPatientSmokingStatus() {
        return smokingStatus;
    }
    /**
     * @return the patient's height
     */
    public Integer getPatientHeight() {
        return height;
    }
    /**
     * @return the patient's weight
     */
    public Integer getPatientWeight() {
        return weight;
    }
    /**
     * @return the patient's medical history
     */
    public String getPatientMedicalHistory() {
        return medicalHistory;
    }
    /**
     * @return the patient's chronic medication field
     */
    public String getPatientChronicMedication() {
        return chronicMedication;
    }
    /**
     * @return the patient's notes
     */
    public String getPatientNotes() {
        return notes;
    }

    /**
     * @return the patient's BMI
     */
    public Double getPatientBMI(){
        if(height==null||weight==null) return null;
        double heightInMeters=height/100.0;
        double bmi=(weight/(heightInMeters*heightInMeters));
        return  Math.round(bmi * 100.0) / 100.0;
    }
}
