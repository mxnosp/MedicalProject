package service;

import model.Visit;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import repository.VisitRepository;

import java.util.List;

public class VisitService {
    private final VisitRepository repo;

    public VisitService(){
        this.repo=new VisitRepository();
    }

    /**
     * Creates a visit object with the given credentials and inserts it to the repository
     * it can throw ValidationException if the credentials were not valid or DBAccessException if the repository throws it
     * @param notes
     * @param paid
     * @param day
     * @param month
     * @param year
     * @param patient_id
     * @throws DBAccessException
     * @throws ValidationException
     */
    public void insertVisit(String notes,boolean paid,int day,int month,int year,int patient_id) throws DBAccessException, ValidationException {
        Visit visit=new Visit(-1,notes,paid,day,month,year,patient_id);
        repo.insertVisit(visit);
    }

    /**
     * Deletes the visit with the given id from the repo ,can throw DBAccessException if repo throws it
     * @param id
     * @throws DBAccessException
     */
    public void deleteVisit(int id)throws DBAccessException{
        repo.deleteVisit(id);
    }

    /**
     *Updates the visit with the given id with the given credentials it can throw DBAccessException if repo throws it
     * or ValidationException if the credentials were not valid
     * @param notes
     * @param paid
     * @param day
     * @param month
     * @param year
     * @param patient_id
     * @param visitid
     * @throws DBAccessException
     * @throws ValidationException
     */
    public void updateVisit(String notes,boolean paid,int day,int month,int year,int patient_id,int visitid) throws ValidationException,DBAccessException{
        Visit updatedVisit=new Visit(visitid,notes,paid,day,month,year,patient_id);
        repo.updateVisit(updatedVisit,visitid);
    }

    /**
     * Returns a list of the visits of the patient with the given id
     * @param patient_id
     * @return
     * @throws DBAccessException
     */
    public List<Visit> getPatientVisits(int patient_id) throws DBAccessException{
        return repo.getPatientVisits(patient_id);
    }
}
