/**
 * CS 460: Secure Communication
 * Professor: Ting Ting Chen
 *
 * Final Project
 *
 * <LoginWindow>
 *
 * Madhav Chhura
 */

package edu.csupomona.cs460.final_project;

/**
 * @author Madhav Chhura 
 *
 */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

//This class is used to create a database where the user information is stored.

public class UserDatabase {
    static final String dBDriver = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/cs460?zeroDateTimeBehavior=convertToNull";

    //Database Credentials
    static final String USER = "root";
    static final String PASS = "";

    Connection conn = null;
    Statement stmt = null;
    String pw = "";
    
    //Constructor for the UserDatabase Class.
    public UserDatabase() {
        getDatabaseReady();
    } 
    
    //Method connects the database, creates a new database, if one doesn't exist.
    private void getDatabaseReady(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
           
            System.out.println("Creating database...");
            stmt = conn.createStatement();
            
            //Used to create a new database..
            String sql = "CREATE DATABASE USERS";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1007) {
                // Database already exists error
                System.out.println("Database already exists.");
                Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    //Closes the database and disconnects.
    public void closeDatabase(){
        try{
            if(stmt!=null)
                    stmt.close();
        }catch(SQLException se2){
            // nothing we can do
        }
        try{
            if(conn!=null)
                    conn.close();
        }catch(SQLException se){
            se.printStackTrace();
        }
    }
    
    //Returns true, if successfully able to login.
    public boolean login (String username, String password){
        int id = 0;
        
        try {
            System.out.print("Logging in..\n");
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE userName= '" + username 
                    + "'and password='" + password + "'");
            
            if (rs.next()) {
                //id = rs.getRow();
                System.out.println("id of the user is " + id);
                System.out.println("Successfully logged in.");
                if(generatePasswords(username))
                    updateDatabase(username,id);
                else
                    System.out.println("Failed to generate password");
                return true;
            }
            else {
                System.out.println("Username and/or password not recognized.");
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }    
    
    // Returns true if the user is added to the Database else 
    // false if the user already exists in the database.
    public boolean addUser(String userName, String password){
        if(checkUser(userName)){
            System.out.print("No User & Password added to the database\n");
            return false;
        }
            
        else {  
            boolean flag =  generatePasswords(userName);
            
            if(flag){
                String sql = "INSERT INTO USERS (userName, password)"
                        + "VALUES('" + userName + "','" + pw + "')";

                try {
                    stmt.execute(sql);
                    System.out.print("Username & Passwords added to the server (database)");
                    return flag;
                } 
                catch (SQLException ex) {
                    Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
            return false;
        }
    }    
    
    //Returns true if password generated and written to a file successfully.
    private boolean generatePasswords(String userName){
        System.out.println("Generating Password!");
        try {
            File file = new File(userName + "_password.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            pw = UUID.randomUUID().toString();
            Sha shaEncrypt = new Sha();
            pw = shaEncrypt.get_SHA_1_SecurePassword(pw);

            bw.write(pw);
            //bw.write("\n");

            System.out.println("Generated password written to a file");
            
            bw.close();
            return true;
        
        } catch (IOException e) {
            System.out.println("Caught Exception during writting to the file");
            e.printStackTrace();
        }
        System.out.println("ERROR: Password generation FAILED.");
        return false;
    }
    
    //Returns true if user exists in the database.
    public boolean checkUser(String username){
        try {  
            System.out.print("Checking if username exists in database\n");
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS");
            while(rs.next()) {
                if(username.equals(rs.getString("userName"))){
                    System.out.print("Username already exists in database\n");
                    return true;
                }
            }
        }
        catch(SQLException e){
        }
        return false;
    }
    
    //Return true if database updated successfully. 
    private boolean updateDatabase(String username, int id) {
        System.out.println("Updating Database!");
        // set the preparedstatement parameters
        try ( // create our java preparedstatement using a sql update query
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE USERS SET password = ? WHERE userName = ?")) {
            // set the preparedstatement parameters
            ps.setString(1, pw);
            ps.setString(2, username);

            // call executeUpdate to execute our sql update statement
            ps.executeUpdate();
            System.out.println("Finished Updating Database!");
            return true;
        }
        catch (SQLException ex) {
            // log the exception
            System.out.println("Caught Exception during updating database!");
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ERROR: Failed to update database!");
        return false;
    }
}