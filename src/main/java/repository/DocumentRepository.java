package repository;

import db.DBConnector;
import model.Document;
import model.exceptions.DBAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentRepository {

    /**
     * Inserts a document in the db can throw DBAccessException if it fails
     * @param doc
     * @throws DBAccessException
     */
    public void insertDoc(Document doc) throws DBAccessException{

        String sql="INSERT INTO documents (patient_id,docpath) VALUES(?,?)";

        try(Connection conn= DBConnector.getConnection(); PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,doc.getPatient_id());
            stmt.setString(2,doc.getDocpath());
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Failed to insert document !");
        }catch(SQLException e){
            throw new DBAccessException("Failed to insert the document!",e);
        }
    }

    /**
     * Deletes the document with the given id can throw DBAcccessException if
     * it can't find the doc or fails to connect to the db
     * @param doc_id
     * @throws DBAccessException
     */
    public void deleteDocument(int doc_id) throws DBAccessException{
        String sql="DELETE FROM documents WHERE id=?";
        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,doc_id);
            int affected=stmt.executeUpdate();
            if(affected==0) throw new DBAccessException("Document not found !");
        }catch(SQLException e){
            throw new DBAccessException("Failed to delete document !",e);
        }
    }

    /**
     * returns a list containing all the documents of the patient with the given id
     * @param patient_id
     * @return
     * @throws DBAccessException
     */
    public List<Document> getPatientDocs(int patient_id)  throws DBAccessException{
        String sql="SELECT id,docpath,patient_id FROM documents WHERE patient_id=?";
        try(Connection conn=DBConnector.getConnection();PreparedStatement stmt=conn.prepareStatement(sql)){
            stmt.setInt(1,patient_id);
            ResultSet rs=stmt.executeQuery();
            ArrayList<Document> docs=new ArrayList<>();
            while(rs.next()){
                docs.add(mapDocument(rs));
            }
            return docs;
        }catch(SQLException e){
            throw new DBAccessException("Failed to fetch documents",e);
        }

    }

    /**
     * maps a result set to a Document object can throw SQLException
     * @param rs
     * @return
     * @throws SQLException
     */
    private Document mapDocument(ResultSet rs) throws SQLException{
        return new Document(rs.getInt("id"),rs.getString("docpath"),rs.getInt("patient_id"));
    }


}
