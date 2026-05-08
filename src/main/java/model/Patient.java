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
    private final String firstname;
    private final String lastname;
    private final String amka;
    private final String  phone;
    private final int id;

    /**
     * model.Patient constructor initializes the patient object
     * it checks that a valid firstname,lastname,amka and phone
     * is given by the user otherwise throws ValidationException
     * @throws InvalidNameException if the first or last name is not valid
     * @throws InvalidAmkaException if the amka is not a 10-digit number
     * @throws InvalidPhoneException if the phone is not a 10=digit number
     * @param firstname
     * @param lastname
     * @param amka
     * @param phone
     */
    public Patient(int id, String firstname, String lastname, String phone, String amka) throws ValidationException {
        if(firstname==null) throw new InvalidNameException("Λάθος Όνομα!");
        if(lastname==null) throw new InvalidNameException("Λάθος Επίθετο!");
        if(amka==null||!amka.matches("\\d{11}")){
            throw new InvalidAmkaException("Λάθος ΑΜΚΑ!");
        }
        if(phone!=null){
            if(!phone.isEmpty()){
                if(!phone.matches("\\d{10}")){
                    throw new InvalidPhoneException("Λάθος τηλέφωνο!");
                }
            }
        }
        this.firstname=firstname;
        this.lastname=lastname;
        this.phone=phone;
        this.amka=amka;
        this.id=id;
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
    public int  getPatientId(){return id;}
}
