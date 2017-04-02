/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drug.pharmacy.team.pkg110;

import java.sql.SQLException;
/**
 *
 * @author Brian
 */
public class Employee 
{
    private int id;
    private String name;
    private  boolean manager; //true if the user is a manager
    private boolean validated; //set to true if user login is successful and member fields are set
    private final Database database;
    
    Employee() throws ClassNotFoundException, SQLException
    {
	database = new Database();
	validated = false;
    }
    //Post: sets member fields if login data matches database data
    void login(int id, String password) throws SQLException, ClassNotFoundException
    {
	boolean validation = false;
	
	validation = validateEmployee(id, password);
	
	if (validation)
	{
	    this.validated = true;
	    this.setID(id);
	    this.setManager(this.readManager(id));
	    this.setName(this.readName(id));
	}
    }
    //Post: returns true if the username/password exist
    //	    otherwise returns false
    boolean validateEmployee(int id, String passwordInput) throws SQLException 
    { 
	String actualPassword;
	
	database.result = database.statement.executeQuery("select password from employee where idemployee = '" + id + "'");
	
	if (database.result.next())
	{
	    actualPassword = database.result.getString(1);
	    
	    if (actualPassword.equals(passwordInput))
	    {
		return true;
	    }
	    else
	    {
		return false;
	    }
	}
	else 
	{
	    return false;
	}
    }
    String readName(int id) throws SQLException
    {
	database.result = database.statement.executeQuery("select name from employee where idemployee = '" + id + "'");
	
	if (database.result.next())
	{
	    return database.result.getString(1);
	}
	else
	{
	    return null;
	}
    }
    boolean readManager(int id) throws SQLException
    {
	database.result = database.statement.executeQuery("select manager from employee where idemployee = '" + id + "'");
	
	if (database.result.next())
	{
	    return database.result.getBoolean(1);
	}
	else
	{
	    return false;
	}
    }
    void setID(int id)
    {
	this.id = id;
    }
    void setName(String name)
    {
	this.name = name;
    }
    void setManager(boolean manager)
    {
	this.manager = manager;
    }
    int getID()
    {
	return id;
    }
    String getName()
    {
	return name;
    }
    boolean getManager()
    {
	return manager;
    }
    boolean checkValidation()
    {
	return validated;
    }
}