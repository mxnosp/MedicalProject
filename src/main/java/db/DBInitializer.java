package db;


import model.exceptions.DBCreationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * DBInitializer class initializes the tables of the db
 */
public final class DBInitializer {

    private DBInitializer(){}

    public static void initializeDB(){
        createPatientsTable();
        createVisitsTable();
        createDocTable();
    }
    
    private static void createPatientsTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS patients (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    phone TEXT ,
                    amka TEXT NOT NULL,
                    search_text TEXT NOT NULL
                );
                """;

        try(Connection conn=DBConnector.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch(SQLException e){
            throw new DBCreationException("Failed to create patients table", e);
        }

        
    }

    private static void createVisitsTable(){

        String sql = """
                CREATE TABLE IF NOT EXISTS visits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER NOT NULL,
                    notes TEXT NOT NULL,
                    paid INTEGER NOT NULL ,
                    day INTEGER NOT NULL,
                    month INTEGER NOT NULL,
                    year INTEGER NOT NULL
                );
                """;

        try(Connection conn=DBConnector.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch(SQLException e){
            throw new DBCreationException("Failed to create visits table", e);
        }

    }

    private static void  createDocTable(){

        String sql = """
                CREATE TABLE IF NOT EXISTS documents (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER NOT NULL,
                    docpath TEXT NOT NULL
                );
                """;

        try(Connection conn=DBConnector.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch(SQLException e){
            throw new DBCreationException("Failed to create documents table", e);
        }


    }
}
