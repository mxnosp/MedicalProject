package service;

import model.Spirometry;
import model.Visit;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import repository.VisitRepository;
import utils.DateParser;

import java.time.LocalDate;
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
     * @param payment
     * @param patient_id
     * @param fef2575
     * @param fev1
     * @param functionalCheck
     * @param fvc
     * @param heartrate
     * @param medication
     * @param pef
     * @param physicalCheck
     * @param reason
     * @param spo2
     * @throws DBAccessException
     * @throws ValidationException
     */
    public void insertVisit(String notes, Integer payment, LocalDate date, int patient_id, Double fev1, Double fvc, Double pef, Double fef2575, Integer heartrate, Integer spo2, String physicalCheck, String functionalCheck, String medication, String reason, LocalDate recheckdate) throws DBAccessException, ValidationException {
        Spirometry spirometry=new Spirometry(fev1,fvc,pef,fef2575);
        Visit visit=new Visit(-1,notes,payment,date,patient_id,spirometry, heartrate, spo2,physicalCheck,functionalCheck,medication, reason, recheckdate);
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
     * @param payment
     * @param medication
     * @param functionalCheck
     * @param reason
     * @param date
     * @param fef2575
     * @param fev1
     * @param fvc
     * @param heartrate
     * @param pef
     * @param physicalCheck
     * @param recheckdate
     * @param spo2
     * @param patient_id
     * @param visitid
     * @throws DBAccessException
     * @throws ValidationException
     */
    public void updateVisit(String notes,Integer payment,LocalDate date,int patient_id,Double fev1,Double fvc,Double pef,Double fef2575,Integer heartrate,Integer spo2, String physicalCheck, String functionalCheck, String medication, String reason,LocalDate recheckdate,int visitid) throws ValidationException,DBAccessException{
        Spirometry spirometry=new Spirometry(fev1,fvc,pef,fef2575);
        Visit updatedVisit=new Visit(visitid,notes,payment, date,patient_id,spirometry, heartrate, spo2,physicalCheck,functionalCheck,medication, reason, recheckdate);
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

    /**
     * deletes all the visits of the patient with the given id
     * @param patient_id
     */
    public void deletePatientVisits(int patient_id) throws DBAccessException{
        List<Visit> visits=repo.getPatientVisits(patient_id);
        while(!visits.isEmpty()){
            repo.deleteVisit(visits.getFirst().getId());
            visits.removeFirst();
        }
    }
}
