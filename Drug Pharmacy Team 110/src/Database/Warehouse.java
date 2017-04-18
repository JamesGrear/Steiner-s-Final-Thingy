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
							+ " FROM warehouse_inventory WHERE (iditem = '" + itemID + "')");
	
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
    public static boolean updateInventory(int itemID, long updateAmount) throws SQLException
    {
	long currentAmount;
	long newAmount;
	
	Database.result = Database.statement.executeQuery("SELECT itemquantity FROM warehouse_inventory WHERE (iditem = '" + itemID + "')");
	if(Database.result.next())
	{
	    currentAmount =  Database.result.getLong(1);
	    newAmount = (currentAmount + updateAmount);
	    
	    Database.statement.executeUpdate("UPDATE warehouse_inventory SET itemquantity = '" + newAmount + "' WHERE iditem = '" + itemID + "'");
	    return true;
	}
	else
	{
	    return false;
	}
    }
    // Pre: The id of the item has already been registered into the Item table
    //Post: if the item id exists in the item table but not the warehouse table, the item and quantity are entered into the warehouse and returns true
    //	    else returns false
    public static boolean registerNewInventory(int itemID, int vendor, long quantity) throws ClassNotFoundException, SQLException
    {
	if(Item.verifyItem(itemID)) //item exists in item table
	{	
	    if (!Warehouse.verifyWarehouseInventory(itemID)) //item doesn't exist in warehouse table
	    {
		Database.statement.executeUpdate("INSERT INTO warehouse_inventory(iditem, vendor, itemquantity)"
					    + "VALUES('" + itemID + "','" + vendor + "','" + quantity + "')");
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
    //Post: if the itemID exists in the warehouse table, returns true
    //	    else returns false
    public static boolean verifyWarehouseInventory(int id) throws SQLException
    {
	Database.result = Database.statement.executeQuery("select iditem from warehouse_inventory where iditem = '" + id + "'"); //kind of redundent, but checks if the id exists
	
	if(Database.result.next())
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
}