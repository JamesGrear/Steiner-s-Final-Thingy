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
public class Warehouse 
{
    //Post: if the id exists, returns the quantity of that item in the warehouse
    //	    else returns -1
    public static int readInventory(int itemID) throws SQLException
    {
	Database.result = Database.statement.executeQuery("SELECT itemquantity"
							+ " FROM warehouse WHERE (iditem = '" + itemID + "')");
	
	if(Database.result.next())
	{
	    return Database.result.getInt(1);
	}
	else
	{
	    return -1;
	}
    }
    //Post: if the id exists, increases its inventory by updateAmount and returns true
    //	    else returns false
    //NOTE: Important to note that updateAmount is not the new value of quantity, it's the amount of change
    //	    if updateAmount = -1, then 1 will be removed from the current inventory
    public static boolean updateInventory(int itemID, int updateAmount) throws SQLException
    {
	int currentAmount;
	int newAmount;
	
	Database.result = Database.statement.executeQuery("SELECT itemquantity"
							+ " FROM warehouse WHERE (iditem = '" + itemID + "')");
	if(Database.result.next())
	{
	    currentAmount =  Database.result.getInt(1);
	    newAmount = (currentAmount + updateAmount);
	    
	    Database.statement.executeUpdate("UPDATE database SET itemquantity = '" + newAmount + "' WHERE iditem = '" + itemID + "'");
	    return true;
	}
	else
	{
	    return false;
	}
    }
    // Pre: The id of the item has already been registered into the Item table
    //Post: if the item id is verified, the item and quantity are entered into the warehouse and returns true
    //	    else returns false
    public static boolean registerNewItem(int itemID, int quantity) throws ClassNotFoundException, SQLException
    {
	if(Item.verifyItem(itemID))
	{	    
	    Database.statement.executeUpdate("INSERT INTO warehouse(iditem, itemquantity)"
					    + "VALUES('" + itemID + "','" + quantity + "')");
	    return true;
	}
	else
	{
	    return false;
	}
    }
}