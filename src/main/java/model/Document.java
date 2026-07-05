package model;

import model.exceptions.DocNotFoundException;

/**
 * model.Document class represents a document of a patient
 */
public class Document {
    private final int doc_id;
    private String docpath;
    private final int patient_id;

    public Document(int doc_id, String docpath, int patient_id) throws DocNotFoundException{
        this.doc_id=doc_id;
        this.patient_id=patient_id;
        if(docpath==null) throw new DocNotFoundException("Δεν είναι δυνατή η φόρτωση του επιλεγμένου εγγράφου!");
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

    /**
     * @return the id of the doc
     */
    public int getDoc_id(){return doc_id;}
}
