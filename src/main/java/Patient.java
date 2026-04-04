import java.util.ArrayList;
import java.util.List;

/**
 * Patient class represents a patient and includes all his personal information and their visits to the
 * doc's office
 */
public class Patient {

    private String firstname;
    private String lastname;
    private long amka;
    private long  phone;
    private ArrayList<Visit> visits;


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

    /**
     * @return the patient's visit list
     */
    public List<Visit> getPatientVisitList(){
        return new ArrayList<Visit>(visits);
    }

}
