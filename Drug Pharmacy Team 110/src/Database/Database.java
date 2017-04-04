/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Brian
 */
public class Database 
{
    protected static Statement statement ;
    protected static ResultSet result;
    
    public static void setupDatabaseConnection() throws ClassNotFoundException, SQLException
    {
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "junkpw");
	    statement = connection.createStatement();
    }
}