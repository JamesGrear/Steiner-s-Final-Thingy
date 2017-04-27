/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Database.Database.connection;

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

	private PreparedStatement selectIDFromCustomer = connection.prepareStatement("SELECT idcustomer FROM customer WHERE name = ? AND address = ?");
	private PreparedStatement insertInCustomer = connection.prepareStatement("INSERT INTO customer(name, address, rewardpoints) VALUES (?, ?, 0)");


    public Customer() throws ClassNotFoundException, SQLException
    {
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
		selectIDFromCustomer.setString(1, name);
		selectIDFromCustomer.setString(2, address);

		Database.result = selectIDFromCustomer.executeQuery();

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
    public void registerNewCustomer() throws SQLException
    {
		insertInCustomer.setString(1, name);
		insertInCustomer.setString(2, name);

		insertInCustomer.executeUpdate();
	}

	//Post: RewardPoints = points + RewardPoints
	public void addRewardPoints(int points) throws SQLException
	{
		int currentPoints;
		int newPoints;

		Database.result = Database.statement.executeQuery("SELECT rewardpoints FROM customer WHERE (idcustomer = '" + id + "')");

		if(Database.result.next())
		{
			currentPoints =  Database.result.getInt(1);
			newPoints = currentPoints + points;

			Database.statement.executeUpdate("UPDATE customer SET rewardpoints = '" + (newPoints) + "' WHERE idcustomer = '" + id + "'");
		}

		rewardPoints = rewardPoints + points; //update current object too
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
		selectIDFromCustomer.setString(1, name);
		selectIDFromCustomer.setString(2, address);

		Database.result = selectIDFromCustomer.executeQuery();

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
    public void setID(int id)
    {
	this.id = id;
    }
    public void setName(String name)
    {
	this.name = name;
    }
    public void setAddress(String address)
    {
	this.address = address;
    }
    public void setRewardPoints(int points)
    {
	this.rewardPoints = points;
    }
}