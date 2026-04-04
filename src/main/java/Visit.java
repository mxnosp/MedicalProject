/**
 * Class Visit represents a visit in the doctor's office
 * by a patient it includes the date if he has paid for the visit
 */

public class Visit {
    private int day;
    private int month;
    private int year;
    private boolean paid;

    /**
     * @return the day of the visit
     */
    public int getDay(){
        return day;
    }
    /**
     * @return the month of the visit
     */
    public int getMonth(){
        return month;
    }
    /**
     * @return the year of the visit
     */
    public int getYear(){
        return year;
    }

    /**
     * @return true if the visit was paid otherwise false
     */
    public boolean isPaidVisit(){
        return paid;
    }
}
