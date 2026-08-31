package service;

import model.Vaccine;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import repository.VaccineRepository;

import java.time.LocalDate;
import java.util.List;

public class VaccineService {
    private final VaccineRepository repo;

    public VaccineService(){
        this.repo=new VaccineRepository();
    }


    /**
     * Creates a vaccine object with the given info and inserts it to the repository
     * it can throw ValidationException if the credentials were not valid or DBAccessException if the repository throws it
     * @param patient_id
     * @param date
     * @param name
     * @param shotnumber
     * @throws DBAccessException
     * @throws ValidationException
     */
    public void insertVaccine(String name, Integer shotnumber, LocalDate date, int patient_id) throws DBAccessException, ValidationException {
        Vaccine vaccine=new Vaccine(name,-1,date,patient_id,shotnumber);
        repo.insertVaccine(vaccine);
    }


    /**
     * Deletes the vaccine with the given id from the repo ,can throw DBAccessException if repo throws it
     * @param id
     * @throws DBAccessException
     */
    public void deleteVaccine(int id)throws DBAccessException{
        repo.deleteVaccine(id);
    }


    /**
     * Returns a list of the vaccines of the patient with the given id
     * @param patient_id
     * @return
     * @throws DBAccessException
     */
    public List<Vaccine> getPatientVaccines(int patient_id) throws DBAccessException{
        return repo.getPatientVaccines(patient_id);
    }
}
