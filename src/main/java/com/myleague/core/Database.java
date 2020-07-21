package com.myleague.core;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Database {
    
    // Parâmetros de ligação da base de dados
    private String dbhost = null;
    private String dbname = null;
    private String dbuser = null;
    private String dbpass = null;
    
    // Atributos de ligação à base de dados
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    public Database() {
        
        this.dbhost = DatabaseConfig.DBHOST;
        this.dbname = DatabaseConfig.DBNAME;
        this.dbuser = DatabaseConfig.DBUSER;
        this.dbpass = DatabaseConfig.DBPASS;
    }
    
    public Database(String dbhost, String dbname, String dbuser, String dbpass) {
        
        this.dbhost = dbhost;
        this.dbname = dbname;
        this.dbuser = dbuser;
        this.dbpass = dbpass;   
    }
    
    public void connect() {
        
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://" + this.dbhost 
                    + "/" + this.dbname +"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", 
                    this.dbuser, this.dbpass);            
            
        } catch (Exception e) {
            
            System.err.println("Database { void connect() }: " +  e.getMessage());
        }
    }
    
    public void prepareStatement(String query) {
        
        try {
            
            this.preparedStatement = this.connect.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            
        } catch (SQLException e) {
            
             System.err.println("Database { void prepareStatement(String query) }: " 
                     +  e.getMessage());
        }        
    }
    
    public int getLastKey() {
        
        ResultSet generatedKeys = null;
        
        try {
            
            generatedKeys =  this.preparedStatement.getGeneratedKeys();
            
            if (generatedKeys.next()) {

                return generatedKeys.getInt(1);   
            }
        
        } catch (SQLException e) {
            
              System.err.println("Database { int getLastKey() }: " + e.getMessage());    
        }
        
        return -1;
    }
    
    public void setString(int index, String parameter) {
        
        try {
            
            this.preparedStatement.setString(index, parameter);
        
        } catch (SQLException e) {
            
              System.err.println("Database { void setString(int index, String paramenter) }: " 
                      +  e.getMessage());    
        }
    }
    
    public void setInt(int index, int parameter) {
        
        try {
            
            this.preparedStatement.setInt(index, parameter);
        
        } catch (SQLException e) {
            
              System.err.println("Database { void setInt(int index, int parameter) }: " 
                      + e.getMessage());    
        }
    }
    
    public void setDate(int index, Date parameter) {
        
        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            this.preparedStatement.setDate(index, new Date(sdf.parse(parameter.toString()).getTime()));
            
        } catch (SQLException e) {
            
            System.err.println("Database { void setDate(int index, String parameter) }: " 
                + e.getMessage()); 
        } catch (ParseException ex) {
             System.err.println("Database { void setDate(int index, String parameter) }: " 
                + ex.getMessage()); 
        }  
    }
    
    public boolean execute() {
        
        boolean result = false;
        
        try {
            
            if (result = this.preparedStatement.execute()) {

                this.resultSet = this.preparedStatement.getResultSet();
            }

        } catch (SQLException e) {
            
              System.err.println("Database { boolean execute() }: " + e.getMessage());
        } 
            
        return result;
    }
    
    public boolean next() {
        
        boolean result = false;
        
        try {

            result = this.resultSet.next();
        
        } catch (SQLException e) {

            System.err.println("Database { boolean next() }: " + e.getMessage());
               e.printStackTrace();
        } 
        
        return result;
    }
    
    public Integer getInt(String column) {
     
        try {
   
            return this.resultSet.getInt(column);
        
        } catch (SQLException e) {

            System.err.println("Database { Integer getInt(String column) }: " + e.getMessage());
        }
        
        return null;
    }
    
    public String getString(String column) {
     
        try {
   
            return this.resultSet.getString(column);
        
        } catch (SQLException e) {

            //System.err.println("Database { String getString(String column) }: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public Date getDate(String column) {
     
        try {
   
            return this.resultSet.getDate(column);
        
        } catch (SQLException e) {

            System.err.println("Database { String getString(String column) }: " + e.getMessage());
        }
        
        return null;
    }
    
    public ResultSet getResultSet() {
        
        return this.resultSet;
    }
    
    public void close() {
        
        try {
            
            this.connect.close();
            
        } catch (SQLException e) {
            
            System.err.println("Database {  void close() }: " + e.getMessage());
        }     
    }
}
