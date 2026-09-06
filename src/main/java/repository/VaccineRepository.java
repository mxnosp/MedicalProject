package repository;

import db.DBConnector;
import model.Spirometry;
import model.Vaccine;
import model.Visit;
import model.exceptions.DBAccessException;
import utils.DateParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class VaccineRepository {

    public VaccineRepository(){ }

    /**
     * inserts the given vaccine to the database can throw DBAccessException if it failed to access the
     * database or no rows were affected after insert
     *
     * @param vaccine
     * @throws DBAccessException
     */
    public void insertVaccine(Vaccine vaccine) throws DBAccessException{
        String sql="INSERT INTO vaccines (patient_id,date,shotnumber,name) VALUES(?,?,?,?)";
        try(Connection conn= DBConnector.getConnection();
            PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,vaccine.getPatientId());
            stmt.setString(2, DateParser.getStringDate(vaccine.getDate()));
            stmt.setInt(3,vaccine.getShotnumber());
            stmt.setString(4, vaccine.getName());
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Failed to insert Vaccine");
        }catch (SQLException e){
            throw new DBAccessException("Failed to insert Vaccine",e);
        }
    }

    /**
     * Deletes the vaccine with the given id can throw DBAccessException if it can't find the vaccine ,or it fails to connect
     * to the db
     * @param id
     * @throws DBAccessException
     */
    public void deleteVaccine(int id) throws DBAccessException{
        String sql="DELETE FROM vaccines WHERE id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Vaccine not found!");
        }catch(SQLException e){
            throw new DBAccessException("Failed to delete vaccine with id "+id,e );
        }
    }

    /**
     * returns all the vaccines the patient with the given id has taken ,if it fails it throws a DBAccessException
     * @param patient_id
     * @return
     * @throws DBAccessException
     */
    public List<Vaccine> getPatientVaccines(int patient_id)throws DBAccessException{

        String sql="SELECT id,date,shotnumber,name FROM vaccines WHERE patient_id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,patient_id);
            ResultSet rs=stmt.executeQuery();
            ArrayList<Vaccine> vaccines=new ArrayList<>();
            while(rs.next()){
                vaccines.add(mapVaccine(rs,patient_id));
            }
            return vaccines;
        }catch (SQLException e){
            throw new DBAccessException("Failed to fetch vaccines for patient "+patient_id);
        }
    }

    /**
     * Takes the vaccine information from a resultset and it creates a vaccine object and returns it
     * @param rs
     * @return
     * @throws SQLException
     */
    private Vaccine mapVaccine(ResultSet rs,int patient_id) throws SQLException{
        return new Vaccine(rs.getString("name"),rs.getInt("id"),DateParser.parseDateFromString(rs.getString("date"))
                ,patient_id,rs.getInt("shotnumber"));
    }

}
