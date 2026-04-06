package repository;

import db.DBConnector;
import model.Patient;
import model.exceptions.DBAccessException;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * PatientRepository class communicates with the db in order to insert,delete,search and update
 * patients it also sets the path of the db
 */
public class PatientRepository {

    public PatientRepository(){ }

    /**
     * inserts the patient given as a parameter to the db
     * @param patient
     */
    public void insertPatient(Patient patient){

        String sql = "INSERT INTO patients(firstname,lastname,phone,amka) VALUES(?,?,?,?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getPatientFirstName());
            stmt.setString(2, patient.getPatientLastName());
            stmt.setLong(3, patient.getPatientPhone());
            stmt.setLong(4, patient.getPatientAmka());

            stmt.execute();
        } catch (SQLException e) {
            throw new DBAccessException("Failed to insert patient "+patient.getPatientFirstName()+" "+patient.getPatientLastName());
        }

    }

    /**
     * deletes the patient with the given id
     * @param id
     */
    public void deletePatient(int id){

        String sql = "DELETE FROM patients WHERE id=?";

        try(Connection conn = DBConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,id);
                stmt.execute();
        }catch (SQLException e){
            throw new DBAccessException("Failed to delete patient with id "+id);
        }
    }

    /**
     * Updates the patient with the given id to the credentials of the Patient object given as a parameter
     * @param updatedpatient
     * @param id
     */
    public void updatePatient(Patient updatedpatient,int id){

        String sql = "UPDATE patients SET first_name = ?, last_name = ?, phone = ? , amka = ? WHERE id = ?";

        try(Connection conn = DBConnector.getConnection(); PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setString(1,updatedpatient.getPatientFirstName());
            stmt.setString(2,updatedpatient.getPatientLastName());
            stmt.setLong(3,updatedpatient.getPatientPhone());
            stmt.setLong(4,updatedpatient.getPatientAmka());
            stmt.execute();
        }catch(SQLException e){
            throw new DBAccessException("Failed to update patient with id "+id);
        }
    }

    public List<Patient> searchPatientsByName(String searchinput){

        
    }

}