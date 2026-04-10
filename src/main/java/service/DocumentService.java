package service;

import model.Document;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import repository.DocumentRepository;

import java.util.List;

public class DocumentService {
    private final DocumentRepository repo;

    public DocumentService(){
        this.repo=new DocumentRepository();
    }

    /**
     * inserts the given document in the repository
     * can throw ValidationException if the attributes are not valid
     * or DBAccessException if the repo throws it
     * @param patient_id
     * @param docpath
     * @throws ValidationException
     * @throws DBAccessException
     */
    public void insertDocument(int patient_id ,String docpath)throws DBAccessException, ValidationException {
        Document doc=new Document(-1,docpath,patient_id);
        repo.insertDoc(doc);
    }

    /**
     * deletes the document with the given id if the doc doesn't exist
     * or repo fails to connect to the db throws DBAccessException
     * @param doc_id
     * @throws DBAccessException
     */
    public void deleteDocument(int doc_id)throws DBAccessException{
        repo.deleteDocument(doc_id);
    }

    /**
     * Returns a list containing the documents of the patient with the
     * given id ,can throw DBAccessException if it fails to connect to the db
     * ,if the patient has no documents returns empty list
     * @param patient_id
     * @return
     */
    public List<Document> getPatientsDocuments(int patient_id){
        return repo.getPatientDocs(patient_id);
    }





}
