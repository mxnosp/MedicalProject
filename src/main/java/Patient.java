import exceptions.InvalidAmkaException;
import exceptions.InvalidNameException;
import exceptions.InvalidPhoneException;


/**
 * Patient class represents a patient and includes all his personal information and their visits to the
 * doc's office
 */
public class Patient {

    private String firstname;
    private String lastname;
    private long amka;
    private long  phone;

    /**
     * Patient constructor initializes the patient object
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
    Patient(String firstname , String lastname ,long amka , long phone ){
        if(firstname==null) throw new InvalidNameException("Not valid first name given!");
        if(lastname==null) throw new InvalidNameException("Not valid last name given!");
        if( amka <= 999999999L || amka >= 10000000000L){
            throw new InvalidAmkaException("Not valid amka given !");
        }
        if( phone <= 999999999L || phone >= 10000000000L){
            throw new InvalidPhoneException("Not valid phone given!");
        }
        this.firstname=firstname;
        this.lastname=lastname;
        this.phone=phone;
        this.amka=amka;
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
    public long  getPatientPhone(){
        return phone;
    }

    /**
     * @return the patient's amka
     */
    public long getPatientAmka(){
        return amka ;
    }

}
