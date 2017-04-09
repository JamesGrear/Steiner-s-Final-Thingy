/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.SQLException;
/**
 *
 * @author Brian
 */
public class Customer 
{
    private int id;
    private String name;
    private String address;
    private int rewardPoints;
    private boolean validated;
    //private Database database;
    
    public Customer() throws ClassNotFoundException, SQLException
    {
	//database = new Database();
	validated = false;
    }
    // Pre: login is being processed with an ID
    //Post: sets member fields if login data matches database data
    public void login(int id) throws SQLException, ClassNotFoundException
    {
	boolean validation = false;
	
	validation = validateCustomer(id);
	
	if (validation)
	{
	    this.validated = true;
	    this.id = id;
	    this.name = readName(id);
	    this.address = readAddress(id);
	    this.rewardPoints = readRewardPoints(id);
	}
    }
    // Pre: login is being proccessed with a name and address
    //Post: sets member fields if login data matches database data
    public void login(String name, String address) throws SQLException
    {
	boolean validation;
	
	validation = validateCustomer(name, address);
	
	if (validation)
	{
	    validated = true;
	    this.name = name;
	    this.address = address;
	    this.id = readID(name, address);
	    this.rewardPoints = readRewardPoints(id);
	}
    }
    //Post: returns true if the id exists
    //	    otherwise returns false
    public boolean validateCustomer(int id) throws SQLException 
    { 	
	Database.result = Database.statement.executeQuery("select idcustomer from customer where idcustomer = '" + id + "'"); //kind of redundent, but checks if the id exists
	
	if (Database.result.next())
	{
	    return true; //as long as the id exists, validation is true
	}
	else 
	{
	    return false;
	}
    }
    //Post: returns true if the name and address exist
    //	    else returns false
    public boolean validateCustomer(String name, String address) throws SQLException 
    { 	
	Database.result = Database.statement.executeQuery("SELECT idcustomer FROM customer WHERE (name = '" + name + "') AND (address = '" + address + "')"); 
	
	if (Database.result.next())
	{
	    return true;
	}
	else 
	{
	    return false;
	}
    }
    //Post: Writes new data into the database for a new Customer
    public void registerNewCustomer(Customer customer) throws SQLException
    {
	Database.statement.executeUpdate("INSERT INTO customer(name, address, rewardpoints)"
			                + "VALUES('" + customer.name + "','" + customer.address + "','" + 0 + "')");
    }
    //Post: RewardPoints = points + RewardPoints
    public void addRewardPoints(int points)
    {
	rewardPoints = rewardPoints + points;
    }
    public String readName(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("SELECT name FROM customer WHERE idcustomer = '" + id + "'");
	
	if (Database.result.next())
	{
	    return Database.result.getString(1);
	}
	else
	{
	    return null;
	}
    }
    public String readAddress(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("select address from customer where idcustomer = '" + id + "'");
	
	if (Database.result.next())
	{
	    return Database.result.getString(1);
	}
	else
	{
	    return null;
	}
    }
    public int readRewardPoints(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("select rewardpoints from customer where idcustomer = '" + id + "'");
	
	if (Database.result.next())
	{
	    return Database.result.getInt(1);
	}
	else
	{
	    return 0;
	}
    }
    public int readID(String name, String address) throws SQLException
    {
	Database.result = Database.statement.executeQuery("SELECT idcustomer FROM customer WHERE (name = '" + name + "') AND (address = '" + address + "')");
	
	if (Database.result.next())
	{
	    return Database.result.getInt(1);
	}
	
	else
	{
	    return 0;
	}
    }
    public boolean checkValidation()
    {
	return validated;
    }
    public int getID()
    {
	return id;
    }
    public String getName()
    {
	return name;
    }
    public String getAddress()
    {
	return address;
    }
    public int getRewardPoints()
    {
	return rewardPoints;
    }
}