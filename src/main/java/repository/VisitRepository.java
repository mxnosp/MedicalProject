package repository;

import db.DBConnector;
import model.Visit;
import model.exceptions.DBAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisitRepository {
    public VisitRepository(){}


    /**
     * inserts the given visit to the database can throw DBAccessException if it failed to access the
     * database or no rows were affected after insert
     *
     * @param visit
     * @throws DBAccessException
     */
    public void insertVisit(Visit visit) throws DBAccessException{
        String sql="INSERT INTO visits (patient_id,notes,paid,day,month,year) VALUES(?,?,?,?,?,?)";

        try(Connection conn= DBConnector.getConnection();
            PreparedStatement stmt=conn.prepareStatement(sql)){

            stmt.setInt(1,visit.getPatientid());
            stmt.setString(2,visit.getVisitNotes());
            int paid=0;
            if(visit.isPaidVisit()) paid=1;
            stmt.setInt(3,paid);
            stmt.setInt(4,visit.getVisitDate().getDay());
            stmt.setInt(5,visit.getVisitDate().getMonth());
            stmt.setInt(6,visit.getVisitDate().getYear());
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Failed to insert Visit");
        }catch (SQLException e){
            throw new DBAccessException("Failed to insert Visit",e);
        }
    }

    /**
     * Deletes the visit with the given id can throw DBAccessException if it can't find the visit ,or it fails to connect
     * to the db
     * @param id
     * @throws DBAccessException
     */
    public void deleteVisit(int id) throws DBAccessException{
        String sql="DELETE FROM visits WHERE id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,id);
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Visit not found!");
        }catch(SQLException e){
            throw new DBAccessException("Failed to delete visit with id "+id,e );
        }
    }

    /**
     * Updates the visit with the given id with the credentials of the given
     * updated visit if it can't find the visit with this id or if it fails
     * to connect to the db throws DBAccessException
     * @param updatedvisit
     * @param id
     */
    public void updateVisit(Visit updatedvisit,int id)  throws DBAccessException{

        String sql="UPDATE visits SET notes=?,paid=?,day=?,month=?,year=? WHERE id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setString(1,updatedvisit.getVisitNotes());
            int paid=0;
            if(updatedvisit.isPaidVisit()) paid=1;
            stmt.setInt(2,paid);
            stmt.setInt(3,updatedvisit.getVisitDate().getDay());
            stmt.setInt(4,updatedvisit.getVisitDate().getMonth());
            stmt.setInt(5,updatedvisit.getVisitDate().getYear());
            stmt.setInt(6,id);
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Visit with the given id not found!");
        }catch (SQLException e){
            throw new DBAccessException("Failed to update visit with id "+id,e);
        }
    }

    /**
     * Returns a list containing the visits of the patient with the given id
     * @param patient_id
     * @return
     */
    public List<Visit> getPatientVisits(int patient_id)throws DBAccessException{

        String sql="SELECT id,notes,paid,day,month,year,patient_id FROM visits WHERE patient_id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,patient_id);
            ResultSet rs=stmt.executeQuery();
            ArrayList<Visit> visits=new ArrayList<>();
            while(rs.next()){
                visits.add(mapVisit(rs));
            }
            return visits;
        }catch (SQLException e){
            throw new DBAccessException("Failed to fetch visits for patient "+patient_id);
        }
    }

    /**
     * Takes the credentials of a result set it creates a visit object and returns it
     * @param rs
     * @return
     * @throws SQLException
     */
    private Visit mapVisit(ResultSet rs) throws SQLException{
        return new Visit(rs.getInt("id"),rs.getString("notes"),
                rs.getInt("paid") == 1,rs.getInt("day"),rs.getInt("month"),
                    rs.getInt("year"),rs.getInt("patient_id"));
    }



}
