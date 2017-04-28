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
public class Warehouse 
{
    int itemID;
    long itemQuantity;
    int vendorCode;
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
     public static ArrayList<Warehouse> readAllInventory() throws SQLException
    {
	ArrayList<Warehouse> list = new ArrayList();
	Warehouse inventory;
	
	Database.result = Database.statement.executeQuery("SELECT iditem, itemquantity, vendor FROM warehouse_inventory WHERE iditem > 0");
	while (Database.result.next())
	{
	    inventory = new Warehouse();

	    inventory.setItemID(Database.result.getInt(1));
	    inventory.setItemQuantity(Database.result.getLong(2));
	    inventory.setVendorCode(Database.result.getInt(3));
	    
	    list.add(inventory);
	}
	
	return list;
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

			if(newAmount >= 0)
			{
				Database.statement.executeUpdate("UPDATE warehouse_inventory SET itemquantity = '" + newAmount + "' WHERE iditem = '" + itemID + "'");
				return true;
			}

			else
				return false;

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
    private void setItemID(int id) 
    {
	this.itemID = id;
    }
    private void setItemQuantity(long quantity)
    {
	this.itemQuantity = quantity;
    }
    private void setVendorCode(int code)
    {
	this.vendorCode = code;
    }
    public int getItemID()
    {
	return itemID;
    }
    public long getItemQuantity()
    {
	return itemQuantity;
    }
    public int getVendorCode()
    {
	return vendorCode;
    }
}