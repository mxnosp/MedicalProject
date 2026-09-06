package service;

import model.Patient;
import model.SmokingStatus;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import repository.PatientRepository;
import java.util.List;

public class PatientService {

    private final PatientRepository repo;

    public PatientService(){
        this.repo=new PatientRepository();
    }

    /**
     * Creates a patient object with the given credentials and adds it to the db it can throw validation exception
     * or DBAccessException
     * @param firstname
     * @param lastname
     * @param phone
     * @param amka
     * @throws ValidationException
     * @throws DBAccessException
     */
    public long insertPatient(String firstname, String lastname, String phone, String amka, SmokingStatus smokingStatus,Integer height,Integer weight,String medicalHistory,String chronicMedication,String notes) throws ValidationException, DBAccessException {
            Integer smoking;
            if(smokingStatus==null) smoking=null;
            else smoking=smokingStatus.ordinal();
            Patient p=new Patient(-1,firstname,lastname,phone,amka,smoking,height,weight,medicalHistory,chronicMedication,notes);
            return repo.insertPatient(p);
    }

    /**
     *Deletes the patient with the given id if the patient is not found or the programm can't access the database it
     * throws DBAccessException
     * @param id
     *
     * @throws DBAccessException
     */
    public void deletePatient(int id ) throws DBAccessException{
            repo.deletePatient(id);
    }

    /**
     * Updates the credentials of the patient with the given id to the ones given as a parameter
     * if the patient is not found, or we cannot access the db throws DBAccessException
     * and if the credentials given are not valid it throws ValidationException
     * @param firstname
     * @param lastname
     * @param phone
     * @param amka
     * @param id
     * @throws ValidationException
     * @throws DBAccessException
     */
    public void updatePatient(String firstname,String lastname,String phone,String amka,SmokingStatus smokingStatus,Integer height,Integer weight,String medicalHistory,String chronicMedication,String notes,int id) throws ValidationException ,DBAccessException{
        Integer smoking;
        if(smokingStatus==null) smoking=null;
        else smoking=smokingStatus.ordinal();
        Patient p=new Patient(-1,firstname,lastname,phone,amka,smoking,height,weight,medicalHistory,chronicMedication,notes);
        repo.updatePatient(p,id);
    }

    /**
     * Searches the database with the given search input and returns a list containg
     * all the patients that where a match ,if it fails to access the db it throws DBAccessException
     * @param search
     * @return
     * @throws DBAccessException
     */
    public List<Patient> searchPatientsByName(String search) throws DBAccessException{
        return repo.searchPatientsByName(search);
    }



    /**
     * Returns a list containing all the patients if the
     * @return
     * @throws DBAccessException
     */
    public List<Patient> getAllPatients() throws DBAccessException{
        return repo.findAllPatients();
    }

    /**
     * searches the db for a patient with the given amka
     * @param amka
     * @return
     */
    public Patient searchPatientByAmka(String amka){
        return repo.searchPatientsByAMKA(amka);
    }

}
