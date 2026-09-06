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
        createVaccineTable();
    }
    
    private static void createPatientsTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS patients (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    first_name TEXT NOT NULL,
                    last_name TEXT NOT NULL,
                    phone TEXT ,
                    amka TEXT NOT NULL,
                    smoking INTEGER,
                    height INTEGER,
                    weight INTEGER,
                    medical_history TEXT,
                    chronic_medication TEXT,
                    notes TEXT,
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
                    notes TEXT ,
                    paid TEXT  ,
                    date TEXT NOT NULL,
                    fev1 TEXT,
                    fvc TEXT,
                    pef TEXT,
                    fef2575 TEXT,
                    heartrate TEXT,
                    spo2 TEXT,
                    physicalcheck TEXT,
                    functionalcheck TEXT,
                    medication TEXT,
                    reason TEXT,
                    recheckdate TEXT
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

    private static void createVaccineTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS vaccines (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    shotnumber INTEGER NOT NULL,
                    name TEXT NOT NULL
                );
                """;

        try(Connection conn=DBConnector.getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        }catch(SQLException e){
            throw new DBCreationException("Failed to create visits table", e);
        }

    }
}
