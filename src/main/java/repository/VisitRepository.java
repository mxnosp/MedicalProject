package repository;

import db.DBConnector;
import model.Spirometry;
import model.Visit;
import model.exceptions.DBAccessException;
import utils.DateParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static utils.NumberInputHelpers.parseDouble;
import static utils.NumberInputHelpers.parseInteger;

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
        String sql="INSERT INTO visits (patient_id,notes,paid,date,fev1,fvc,pef,fef2575,heartrate,spo2,physicalcheck,functionalcheck,medication,reason,recheckdate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection conn= DBConnector.getConnection();
            PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,visit.getPatientid());
            stmt.setString(2,visit.getVisitNotes());
            setNullableString(stmt,3,visit.getVisitPayment());
            stmt.setString(4,DateParser.getStringDate(visit.getVisitDate()));
            setNullableString(stmt,5,visit.getSpirometry().getFEV1());
            setNullableString(stmt,6,visit.getSpirometry().getFVC());
            setNullableString(stmt,7,visit.getSpirometry().getPEF());
            setNullableString(stmt,8,visit.getSpirometry().getFEF2575());
            setNullableString(stmt,9,visit.getHeartRate());
            setNullableString(stmt,10,visit.getSpo2());
            stmt.setString(11,visit.getPhysicalCheck());
            stmt.setString(12,visit.getFunctionalCheck());
            stmt.setString(13,visit.getMedication());
            stmt.setString(14,visit.getReason());
            stmt.setString(15,DateParser.getStringDate(visit.getReappoinment()));
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

        String sql="UPDATE visits SET notes=?,paid=?,date = ?,fev1=?,fvc=?,pef=?,fef2575=?,heartrate=?,spo2=?,physicalcheck=?,functionalcheck=?,medication=?,reason=?, recheckdate= ?" +
                " WHERE id=?";

        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,updatedvisit.getPatientid());
            stmt.setString(2,updatedvisit.getVisitNotes());
            setNullableString(stmt,3,updatedvisit.getVisitPayment());
            stmt.setString(4,DateParser.getStringDate(updatedvisit.getVisitDate()));
            setNullableString(stmt,5,updatedvisit.getSpirometry().getFEV1());
            setNullableString(stmt,6,updatedvisit.getSpirometry().getFVC());
            setNullableString(stmt,7,updatedvisit.getSpirometry().getPEF());
            setNullableString(stmt,8,updatedvisit.getSpirometry().getFEF2575());
            setNullableString(stmt,9,updatedvisit.getHeartRate());
            setNullableString(stmt,10,updatedvisit.getSpo2());
            stmt.setString(11,updatedvisit.getPhysicalCheck());
            stmt.setString(12,updatedvisit.getFunctionalCheck());
            stmt.setString(13,updatedvisit.getMedication());
            stmt.setString(14,updatedvisit.getReason());
            stmt.setString(15,DateParser.getStringDate(updatedvisit.getReappoinment()));
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

        String sql="SELECT id,notes,paid,date,patient_id,fev1,fvc,pef,fef2575,heartrate,spo2,physicalcheck,functionalcheck,medication,reason,recheckdate FROM visits WHERE patient_id=?";

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
        Spirometry spirometry=new Spirometry(parseDouble(rs.getString("fev1")),parseDouble(rs.getString("fvc")),parseDouble(rs.getString("pef")),parseDouble(rs.getString("fef2575")));
        return new Visit(rs.getInt("id"),rs.getString("notes"),
                parseInteger(rs.getString("paid")), DateParser.parseDateFromString(rs.getString("date"))
                ,rs.getInt("patient_id"),spirometry,parseInteger(rs.getString("heartrate")),parseInteger(rs.getString("spo2")),
                rs.getString("physicalcheck"),rs.getString("functionalcheck"),rs.getString("medication"),rs.getString("reason"),
                DateParser.parseDateFromString(rs.getString("recheckdate")));

    }

    private static void setNullableString(PreparedStatement stmt, int index, Object value)
            throws SQLException {

        if (value == null) {
            stmt.setNull(index, Types.VARCHAR);
        } else {
            stmt.setString(index, value.toString());
        }
    }


}
