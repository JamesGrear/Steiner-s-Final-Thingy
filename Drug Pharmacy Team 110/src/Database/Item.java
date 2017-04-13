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
    private int id; //can only be changed by changing the id in the database. that way false data doesn't exist in item objects
    private String name;
    private String description;
    private int dosage;
    private int warning;
    private double cost;
    private int reorderLevel;
    private int reorderQuantity;
    private int vendor;
    private String deliveryTime;
    
    public Item(int id) throws ClassNotFoundException, SQLException
    {
		this.id = id;
    }
    //Post: if the ID doesn't exist, adds a new item into the database and returns true
    //	    if the ID already exists, returns false
    public boolean registerNewItem() throws SQLException
    {
		if(!verifyItem(id))
		{
			Database.statement.executeUpdate
					("INSERT INTO item(iditem, name, description, dosage, warning, cost, reorderlevel, reorderquantity, vendor, deliverytime)"
						+ "VALUES('" + id + "','" + name + "','" + description + "','" + dosage + "','" + warning + "','"
					+ cost + "','" + reorderLevel + "','" + reorderQuantity + "','" + vendor + "','" + deliveryTime + "')");
			return true;
		}
		else
		{
			return false;
		}
    }
    //Post: if the ID exists, deletes the pre existing item from the database 
    //	    if the ID did not exist, returns false
    public boolean deleteItem() throws SQLException
    {
		if(verifyItem(id))
		{
			Database.statement.executeUpdate("DELETE FROM item WHERE iditem = '" + id + "'");

			return true;
		}
		else
		{
			return false;
		}
    }
    //Post: if the item exists, updates all attributes of a pre-existing item (except id)and returns true
    //	    if the item did not exist, returns false
    public boolean updateItem() throws SQLException
    {
	if(verifyItem(id))
	{	    
	    Database.statement.executeUpdate("Update item Set name = '" + name + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set description = '" + description + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set dosage = '" + dosage + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set warning = '" + warning + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set cost = '" + cost + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set reorderlevel = '" + reorderLevel + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set reorderquantity = '" + reorderQuantity + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set vendor = '" + vendor + "' WHERE iditem = '" + id + "'");
	    Database.statement.executeUpdate("Update item Set deliverytime = '" + deliveryTime + "' WHERE iditem = '" + id + "'");
	            
	    return true;
	}
	else
	{
	    return false;
	}
    }
    //Post: if the id exists and newID doesn't, updates the id of a pre-existing item to newID in the database, updates object id to newID, returns true
    //	    else, returns false
    public boolean updateItemID(int newID) throws SQLException
    {
	if(verifyItem(id))
	{	    
	    if (!verifyItem(newID))
	    {
		Database.statement.executeUpdate("UPDATE item SET iditem = '" + newID + "' WHERE iditem = '" + this.id + "'");
		this.id = newID;
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
    //Pre : This is a private static method. It is meant only for internal data verification
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
    //Pre : Static method, only called at class level. Returns an initialized item. Initializing item previously to calling this isn't necessary
    //	    i.e. Item item = Item.readItem(id);
    //Post: If an item with the passed in id exists in the database, an item object is returned with all fields set from database
    //	    else a null object is returned;
    public static Item readItem(int id) throws ClassNotFoundException, SQLException
    {
		Item item;

		if (verifyItem(id))
		{
			Database.result = Database.statement.executeQuery("SELECT name, description, dosage, warning, cost, reorderlevel, reorderquantity, vendor, deliverytime"
								+ " FROM item WHERE (iditem = '" + id + "')");

			if (Database.result.next())
			{
				item = new Item(id);

				item.name = Database.result.getString(1);
				item.description = Database.result.getString(2);
				item.dosage = Database.result.getInt(3);
				item.warning = Database.result.getInt(4);
				item.cost = Database.result.getDouble(5);
				item.reorderLevel = Database.result.getInt(6);
				item.reorderQuantity = Database.result.getInt(7);
				item.vendor = Database.result.getInt(8);
				item.deliveryTime = Database.result.getString(9);

				return item;
			}
		}

		return null; //if all else fails
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