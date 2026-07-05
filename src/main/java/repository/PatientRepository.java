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
    public void insertPatient(Patient patient) throws DBAccessException, PatientExistsException {

        if (patient.getPatientAmka() != null && searchPatientsByAMKA(patient.getPatientAmka()) != null) {
            throw new PatientExistsException("Υπάρχει ήδη ασθενής με αυτόν τον ΑΜΚΑ!");
        }

        String sql = """
            INSERT INTO patients (
                first_name,
                last_name,
                phone,
                amka,
                smoking,
                height,
                weight,
                medical_history,
                chronic_medication,
                notes,
                search_text
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String firstName = patient.getPatientFirstName();
            String lastName = patient.getPatientLastName();

            String searchText = normalizeGreekSearchText(
                    firstName + " " +lastName
            );

            setNullableString(stmt, 1, firstName);
            setNullableString(stmt, 2, lastName);
            setNullableString(stmt, 3, patient.getPatientPhone());
            setNullableString(stmt, 4, patient.getPatientAmka());

            if (patient.getPatientSmokingStatus() == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, patient.getPatientSmokingStatus().ordinal());
            }

            setNullableInteger(stmt, 6, patient.getPatientHeight());
            setNullableInteger(stmt, 7, patient.getPatientWeight());

            setNullableString(stmt, 8, patient.getPatientMedicalHistory());
            setNullableString(stmt, 9, patient.getPatientChronicMedication());
            setNullableString(stmt, 10, patient.getPatientNotes());
            setNullableString(stmt, 11, searchText);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DBAccessException("Insert failed: no rows affected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBAccessException(
                    "Failed to insert patient " +
                            patient.getPatientFirstName() + " " +
                            patient.getPatientLastName(),
                    e
            );
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
    public void updatePatient(Patient updatedPatient, int id) throws DBAccessException {
        String sql = """
            UPDATE patients
            SET first_name = ?,
                last_name = ?,
                phone = ?,
                amka = ?,
                smoking = ?,
                height = ?,
                weight = ?,
                medical_history = ?,
                chronic_medication = ?,
                notes = ?,
                search_text = ?
            WHERE id = ?
            """;

        Patient patientExists = searchPatientsByAMKA(updatedPatient.getPatientAmka());

        if (patientExists != null && patientExists.getPatientId() != id) {
            throw new PatientExistsException("Υπάρχει ήδη ασθενής με αυτόν τον ΑΜΚΑ!");
        }

        String searchText = normalizeGreekSearchText(
                updatedPatient.getPatientFirstName()+ " " +
                        updatedPatient.getPatientLastName()
        );

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setNullableString(stmt, 1, updatedPatient.getPatientFirstName());
            setNullableString(stmt, 2, updatedPatient.getPatientLastName());
            setNullableString(stmt, 3, updatedPatient.getPatientPhone());
            setNullableString(stmt, 4, updatedPatient.getPatientAmka());

            if (updatedPatient.getPatientSmokingStatus() == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, updatedPatient.getPatientSmokingStatus().ordinal());
            }

            setNullableInteger(stmt, 6, updatedPatient.getPatientHeight());
            setNullableInteger(stmt, 7, updatedPatient.getPatientWeight());

            setNullableString(stmt, 8, updatedPatient.getPatientMedicalHistory());
            setNullableString(stmt, 9, updatedPatient.getPatientChronicMedication());
            setNullableString(stmt, 10, updatedPatient.getPatientNotes());
            setNullableString(stmt, 11, searchText);

            stmt.setInt(12, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DBAccessException("Update failed: no patient found with id " + id);
            }

        } catch (SQLException e) {
            throw new DBAccessException("Failed to update patient with id " + id, e);
        }
    }

    /**
     * Searches the db to find if the input given exists inside the search_text of a patient or more and then returns a list containing the matching patients or patient if
     * the input is blank or null returns a list containing all the patients
     * @param searchInput
     * @return
     */
    public List<Patient> searchPatientsByName(String searchInput) throws DBAccessException{
        String sql;
        if (searchInput == null || searchInput.isBlank()) {
            sql="""
            SELECT id, first_name, last_name, phone, amka,smoking,height,weight,medical_history,chronic_medication,notes
            FROM patients
            """;
            List<Patient> patients = new ArrayList<>();

            try (
                    Connection conn = DBConnector.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)
            ) {
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

        String normalizedInput = normalizeGreekSearchText(searchInput);

        sql = """
            SELECT id, first_name, last_name, phone,amka,smoking,height,weight,medical_history,chronic_medication,notes
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
            SELECT id, first_name, last_name, phone, amka,smoking,height,weight,medical_history,chronic_medication,notes
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
                getNullableString(rs, "first_name"),
                getNullableString(rs, "last_name"),
                getNullableString(rs, "phone"),
                getNullableString(rs, "amka"),
                getNullableInteger(rs, "smoking"),
                getNullableInteger(rs, "height"),
                getNullableInteger(rs, "weight"),
                getNullableString(rs, "medical_history"),
                getNullableString(rs, "chronic_medication"),
                getNullableString(rs, "notes")
        );
    }

    /**
     * searches the db for a patient with the given amka if the patient doesn't exist returns null
     * @param amka
     * @return
     * @throws DBAccessException
     */
    public Patient searchPatientsByAMKA(String amka) throws DBAccessException {
        if (amka == null || amka.isBlank()) {
            return null;
        }

        String normalizedAmka = amka.trim();

        if (!normalizedAmka.matches("\\d{11}")) {
            return null;
        }

        String sql = """
            SELECT id,
                   first_name,
                   last_name,
                   phone,
                   amka,
                   smoking,
                   height,
                   weight,
                   medical_history,
                   chronic_medication,
                   notes
            FROM patients
            WHERE amka = ?
            """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, normalizedAmka);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DBAccessException("Failed searching patients", e);
        }
    }

    private static void setNullableString(PreparedStatement stmt, int index, String value)
            throws SQLException {

        if (value == null || value.isBlank()) {
            stmt.setNull(index, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(index, value.trim());
        }
    }

    private static void setNullableInteger(PreparedStatement stmt, int index, Integer value)
            throws SQLException {

        if (value == null) {
            stmt.setNull(index, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

    private static Integer getNullableInteger(ResultSet rs, String column)
            throws SQLException {

        int value = rs.getInt(column);

        if (rs.wasNull()) {
            return null;
        }

        return value;
    }

    private static String getNullableString(ResultSet rs, String column)
            throws SQLException {

        String value = rs.getString(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}

