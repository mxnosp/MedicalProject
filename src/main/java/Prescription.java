/**
 * Prescription class represents a prescription written by the doctor
 * it includes the medicine prescribed the period the patient needs to take the medicine
 * the date it was prescripted and instructions for the usage of the medicine
 */
public class Prescription {
    private final int patient_id;
    private String medicinename;
    private Date currentDate;
    private Date endofprescriptionDate;
    private String instructions ;

    Prescription(int patient_id,String medicinename,int startday,int startmonth,int startyear,int endday,int endmonth,int endyear,String instructions){
        this.patient_id=patient_id;

    }




    public String getMedicinename() {
        return medicinename;
    }



    public String getInstructions() {
        return instructions;
    }
}
