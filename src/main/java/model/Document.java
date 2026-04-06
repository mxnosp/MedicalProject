package model;

import model.exceptions.DocNotFoundException;

/**
 * model.Document class represents a document of a patient
 */
public class Document {
    private String docpath;
    private final int patient_id;

    Document(String docpath,int patient_id) throws DocNotFoundException{
        this.patient_id=patient_id;
        if(docpath==null) throw new DocNotFoundException("The selected document cannot be loaded!");
        this.docpath=docpath;
    }

    /**
     * @return the path of the document
     */
    public String getDocpath(){return docpath;}

    /**
     * @return the id of the patient that the document belongs to
     */
    public int getPatient_id(){return patient_id;}
}
