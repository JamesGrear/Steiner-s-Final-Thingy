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
public class Item 
{
    private int id;
    private String name;
    private String description;
    private int dosage;
    private int warning;
    private double cost;
    private int reorderLevel;
    private int reorderQuantity;
    private int vendor;
    private String deliveryTime;
    
    public Item() throws ClassNotFoundException, SQLException
    {
    }
    //Post: adds a new item into the database if the ID didn't already exist. 
    //	    if the ID already exists, returns false
    public boolean registerNewItem(Item item) throws SQLException
    {
	if(!verifyItem(item.id))
	{
	    Database.statement.executeUpdate
				("INSERT INTO item(iditem, name, description, dosage, warning, cost, reorderlevel, reorderquantity, vendor, deliverytime)"
			        + "VALUES('" + item.id + "','" + item.name + "','" + item.description + "','" + item.dosage + "','" + item.warning + "','" 
				+ item.cost + "','" + item.reorderLevel + "','" + item.reorderQuantity + "','" + item.vendor + "','" + item.deliveryTime + "')");
	    return true;
	}
	else
	{
	    return false;
	}
    }
    //Pre : This is a static method. It can only be called from the class itself. Item.readID(id)
    //Post: Returns true if an item with the id exists, else returns false
    public static boolean verifyItem(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("select iditem from item where iditem = '" + id + "'"); //kind of redundent, but checks if the id exists
	
	if(Database.result.next())
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
    public int getID()
    {
	return id;
    }
    public String getName()
    {
	return name;
    }
    public int getWarning()
    {
	return warning;
    }
    public double getCost()
    {
	return cost;
    }
     public String setDeliveryTime()
    {
	return deliveryTime;
    }
    public String setDescription()
    {
	return description;
    }
    public int setDosage()
    {
	return dosage;
    }
    public int setReorderLevel()
    {
	return reorderLevel;
    }
    public int setReorderQuantity()
    {
	return reorderQuantity;
    }
    public int setVendor()
    {
	return vendor;
    }
    public void setID(int id)
    {
	this.id = id;
    }
    public void setName(String name)
    {
	this.name = name;
    }
    public void setWarning(int warning)
    {
	this.warning = warning;
    }
    public void setCost(double cost)
    {
	this.cost = cost;
    }
    public void setDeliveryTime(String time)
    {
	this.deliveryTime = time;
    }
    public void setDescription(String description)
    {
	this.description = description;
    }
    public void setDosage(int dosage)
    {
	this.dosage = dosage;
    }
    public void setReorderLevel(int reorder)
    {
	this.reorderLevel = reorder;
    }
    public void setReorderQuantity(int quantity)
    {
	this.reorderQuantity = quantity;
    }
    public void setVendor(int vendor)
    {
	this.vendor = vendor;
    }
}