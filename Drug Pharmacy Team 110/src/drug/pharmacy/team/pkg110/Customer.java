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
public class Customer 
{
    private int id;
    private String name;
    private String address;
    private int rewardPoints;
    private boolean validated;
    //private Database database;
    
    Customer() throws ClassNotFoundException, SQLException
    {
	//database = new Database();
	validated = false;
    }
    // Pre: login is being processed with an ID
    //Post: sets member fields if login data matches database data
    void login(int id) throws SQLException, ClassNotFoundException
    {
	boolean validation = false;
	
	validation = validateCustomer(id);
	
	if (validation)
	{
	    this.validated = true;
	    this.setID(id);
	    this.setName(this.readName(id));
	    this.setAddress(this.readAddress(id));
	    this.setRewardPoints(this.readRewardPoints(id));
	}
    }
    // Pre: login is being proccessed with a name and address
    //Post: sets member fields if login data matches database data
    void login(String name, String address) throws SQLException
    {
	boolean validation = false;
	
	validation = validateCustomer(name, address);
	
	if (validation)
	{
	    this.validated = true;
	    this.setName(name);
	    this.setAddress(address);
	    this.setID(this.readID(name, address));
	    this.setRewardPoints(this.readRewardPoints(id));
	}
    }
    //Post: returns true if the id exists
    //	    otherwise returns false
    boolean validateCustomer(int id) throws SQLException 
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
    boolean validateCustomer(String name, String address) throws SQLException 
    { 	
	String dbAddress;
	
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
    void registerNewCustomer(String name, String address) throws SQLException
    {
	Database.statement.executeUpdate("insert into customer(name, address, rewardpoints)"
			                + "VALUES('" + name + "','" + address + "','" + 0 + "')");
    }
    //Post: RewardPoints = points + RewardPoints
    void addRewardPoints(int points)
    {
	rewardPoints = rewardPoints + points;
    }
    String readName(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("select name from customer where idcustomer = '" + id + "'");
	
	if (Database.result.next())
	{
	    return Database.result.getString(1);
	}
	else
	{
	    return null;
	}
    }
    String readAddress(int id) throws SQLException
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
    int readRewardPoints(int id) throws SQLException
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
    int readID(String name, String address) throws SQLException
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
    boolean checkValidation()
    {
	return validated;
    }
    void setID(int id)
    {
	this.id = id;
    }
    void setName(String name)
    {
	this.name = name;
    }
    void setAddress(String address)
    {
	this.address = address;
    }
    void setRewardPoints(int points)
    {
	this.rewardPoints = points;
    }
    int getID()
    {
	return id;
    }
    String getName()
    {
	return name;
    }
    String getAddress()
    {
	return address;
    }
    int getRewardPoints()
    {
	return rewardPoints;
    }
}