package repository;

import db.DBConnector;
import model.Date;
import model.Visit;
import model.exceptions.DBAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            throw new DBAccessException("Failed to delete visit with id "+id );
        }
    }

    public void updateVisit(String notes, int paid, Date date){

        String sql="UPDATE visits SET notes=?,paid=?,day=?,month=?,year=? WHERE id=?";

        //try(Connection conn=DBConnector.getConnection();PreparedStatement stmt )
    }


}
