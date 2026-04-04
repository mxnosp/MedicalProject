/**
 * Class Visit represents a visit in the doctor's office
 * by a patient it includes the date if he has paid for the visit
 */

public class Visit {
    private Date date;
    private boolean paid;


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
}
