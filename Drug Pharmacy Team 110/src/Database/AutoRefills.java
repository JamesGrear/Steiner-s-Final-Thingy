/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Brian
 */
public class AutoRefills 
{
    private int id;
    private Customer customer;
    private Item item;
    private int ammount;
    private int frequency; //int days
    private int daysUntil; //time before next refill, can go below 0 if a refill is delayed due to lack of inventory
    private int remainingRefills;
 
    public void registerNewAutoRefill() throws SQLException
    {
	Database.statement.executeUpdate
		("INSERT INTO auto_refills(iditem, idcustomer, frequency, daysuntil, remainingrefills, amount)"
		+ "VALUES('" + item.getID() + "','" + customer.getID() + "','" + frequency + "','" + daysUntil + "','" + remainingRefills + "','" + ammount + "')");
    }
    public void deleteAutoRefill() throws SQLException
    { 
	Database.statement.executeUpdate("DELETE FROM auto_refills WHERE idrefill = '" + id + "'");
    }

    //Post: Returns all auto refills where daysUntil <= 0
    public static ArrayList<AutoRefills> readAutoRefills() throws SQLException, ClassNotFoundException
    {
        ArrayList<AutoRefills> refills = new ArrayList<>();
        Item item;
        Customer customer;
        AutoRefills refill;

        Database.result2 = Database.statement2.executeQuery("SELECT idrefill, iditem, idcustomer, frequency, daysuntil, remainingrefills, ammount"
                                + " FROM auto_refills WHERE (daysuntil <= '" + 0 + "')");

        while(Database.result2.next())
        {
            refill = new AutoRefills();
            customer = new Customer();

            refill.id = Database.result2.getInt(1);
            item = Item.readItem(Database.result2.getInt(2)); //read the item id and create an item object with correct variables
            customer.login(Database.result2.getInt(3)); //read the customer id and create a customer object with correct variables
            refill.frequency = Database.result2.getInt(4);
            refill.daysUntil = Database.result2.getInt(5);
            refill.remainingRefills = Database.result2.getInt(6);
            refill.ammount = Database.result2.getInt(7);

            refill.item = item;
            refill.customer = customer;
            refills.add(refill);
        }

        return refills;
    }
    public void updateDaysUntil(int days) throws SQLException
    {
	int currentDays;
	int newDays;
	
	Database.result = Database.statement.executeQuery("SELECT daysuntil FROM auto_refills WHERE (idrefill = '" + id + "')");
	
	if(Database.result.next())
	{
	    currentDays =  Database.result.getInt(1);
	    newDays = currentDays + days;
	    
	    Database.statement.executeUpdate("UPDATE auto_refills SET daysuntil = '" + (newDays) + "' WHERE idrefill = '" + id + "'");
	    daysUntil--; //update current object too
	}
    }
    public void updateRefillsRemaining(int remaining) throws SQLException
    {
	int currentRefills;
	int newRefills;
	
	Database.result = Database.statement.executeQuery("SELECT remainingrefills FROM auto_refills WHERE (idrefill = '" + id + "')");
	
	if(Database.result.next())
	{
	    currentRefills = Database.result.getInt(1);
	    newRefills = currentRefills + remaining;
	    
	    Database.statement.executeUpdate("UPDATE auto_refills SET remainingrefills = '" + (newRefills) + "' WHERE idrefill = '" + id + "'");
	    remainingRefills--; //update current object too
	}
    }
    public int getID()
    {
	return id;
    }
    public Item getItem()
    {
	return item;
    }
    public Customer getCustomer()
    {
	return customer;
    }
    public int getFrequency()
    {
	return frequency;
    }
    public int getDaysUntil()
    {
	return daysUntil;
    }
    public int getRemainingRefills()
    {
	return remainingRefills;
    }
    public int getAmmount()
    {
	return ammount;
    }
    public void setItem(Item item)
    {
	this.item = item;
    }
    public void setCustomer(Customer customer)
    {
	this.customer = customer;
    }
    public void setFrequency(int frequency)
    {
	this.frequency = frequency;
    }
    public void setDaysUntil(int days)
    {
	this.daysUntil = days;
    }
    public void setRemainingRefills(int remaining)
    {
	this.remainingRefills = remaining;
    }
    public void setAmmount(int ammount)
    {
	this.ammount = ammount;
    }
}