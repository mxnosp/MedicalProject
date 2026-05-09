package repository;

import db.DBConnector;
import model.Patient;
import model.exceptions.DBAccessException;
import model.exceptions.PatientExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static utils.TextNormalizer.normalizeGreekSearchText;

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
    public void insertPatient(Patient patient) throws DBAccessException,PatientExistsException{

        if(searchPatientsByAMKA(patient.getPatientAmka())!=null){
            throw new PatientExistsException("Patient with the given AMKA already exists!");
        }

        String sql = "INSERT INTO patients(first_name,last_name,phone,amka,search_text) VALUES(?,?,?,?,?)";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchtext=normalizeGreekSearchText(patient.getPatientFirstName()+" "+patient.getPatientLastName());
            stmt.setString(1, patient.getPatientFirstName());
            stmt.setString(2, patient.getPatientLastName());
            stmt.setString(3, patient.getPatientPhone());
            stmt.setString(4, patient.getPatientAmka());
            stmt.setString(5,searchtext);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DBAccessException("Insert failed: no rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBAccessException("Failed to insert patient "+patient.getPatientFirstName()+" "+patient.getPatientLastName());
        }

    }

    /**
     * deletes the patient with the given id
     * @param id
     */
    public void deletePatient(int id) throws DBAccessException{

        String sql = "DELETE FROM patients WHERE id=?";

        try(Connection conn = DBConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,id);
                int affected=stmt.executeUpdate();
                if(affected==0){
                    throw new DBAccessException("DELETE failed: no patient found with id " + id);
                }
        }catch (SQLException e){
            throw new DBAccessException("Failed to delete patient with id "+id);
        }
    }

    /**
     * Updates the patient with the given id to the credentials of the Patient object given as a parameter
     * @param updatedPatient
     * @param id
     */
    public void updatePatient(Patient updatedPatient, int id) throws DBAccessException{
        String sql = """
            UPDATE patients
            SET first_name = ?, last_name = ?, phone = ?, amka = ?, search_text = ?
            WHERE id = ?
            """;

        String searchText = normalizeGreekSearchText(
                updatedPatient.getPatientFirstName() + " " + updatedPatient.getPatientLastName()
        );

        try (
                Connection conn = DBConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, updatedPatient.getPatientFirstName());
            stmt.setString(2, updatedPatient.getPatientLastName());
            stmt.setString(3, updatedPatient.getPatientPhone());
            stmt.setString(4, updatedPatient.getPatientAmka());
            stmt.setString(5, searchText);
            stmt.setInt(6, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DBAccessException("Update failed: no patient found with id " + id);
            }
        } catch (SQLException e) {
            throw new DBAccessException("Failed to update patient with id " + id, e);
        }
    }

    /**
     * Searches the db to find if the input given exists inside the search_text of a patient or more and then returns a list containing the matching patients or patient
     * @param searchInput
     * @return
     */
    public List<Patient> searchPatientsByName(String searchInput) throws DBAccessException{
        if (searchInput == null || searchInput.isBlank()) {
            return List.of();
        }

        String normalizedInput = normalizeGreekSearchText(searchInput);

        String sql = """
            SELECT id, first_name, last_name, phone, amka
            FROM patients
            WHERE search_text LIKE ?
            """;

        List<Patient> patients = new ArrayList<>();

        try (
                Connection conn = DBConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "%" + normalizedInput + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
            }

            return patients;
        } catch (SQLException e) {
            throw new DBAccessException("Failed searching patients", e);
        }
    }

    /**
     * @return returns a list containing all the patients of the table
     */
    public List<Patient> findAllPatients() throws DBAccessException{
        String sql = """
            SELECT id, first_name, last_name, phone, amka
            FROM patients
            """;

        List<Patient> patients = new ArrayList<>();

        try (
                Connection conn = DBConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                patients.add(mapPatient(rs));
            }

            return patients;
        } catch (SQLException e) {
            throw new DBAccessException("Failed fetching all patients", e);
        }
    }

    /**
     * Takes a resultset of the table and creates a patient with the result's sets elements
     * @param rs
     * @return
     * @throws SQLException
     */
    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("amka")
        );
    }

    /**
     * searches the db for a patient with the given amka if the patient doesn't exist returns null
     * @param amka
     * @return
     * @throws DBAccessException
     */
    public Patient searchPatientsByAMKA(String amka) throws DBAccessException{
        if (amka == null || amka.isBlank()) {
            return null;
        }

        String sql = """
            SELECT id, first_name, last_name, phone
            FROM patients
            WHERE amka LIKE ?
            """;

        Patient patient=null;
        try (
                Connection conn = DBConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1,amka);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    patient=new Patient(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("phone"),
                            amka
                    );
                }

            }
            return patient;
        } catch (SQLException e) {
            throw new DBAccessException("Failed searching patients", e);
        }
    }
}

