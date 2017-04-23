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
public class StoreInventory 
{
    private int storeID;
    private int itemID;
    private long itemQuantity;
    private long defaultQuantity;
    private long reorderLevel;
    private long reorderQuantity;
    
    // Pre: The id of the item has already been registered into the Item table
    //Post: if the item id exists in the item table but not the store_inventory table, the item and quantity are entered into the warehouse and returns true
    //	    else returns false
    public boolean registerNewInventory() throws ClassNotFoundException, SQLException
    {
      if(Item.verifyItem(itemID)) //item exists in item table
      {	
          if (!StoreInventory.verifyStoreInventory(storeID, itemID)) //item doesn't exist in warehouse table
          {
            Database.statement.executeUpdate("INSERT INTO store_inventory(idstore, iditem, itemquantity, defaultquantity, reorderlevel, reorderquantity)"
                      + "VALUES('" + storeID + "','" + itemID + "','" + itemQuantity + "','" + defaultQuantity + "','" + reorderLevel + "','" + reorderQuantity + "')");
            
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
  
    //Post: if the id exists, increases its inventory by updateAmount and returns true
    //	    else returns false
    //NOTE: Important to note that updateAmount is not the new value of quantity, it's the amount of change
    //	    if updateAmount = -1, then 1 will be removed from the current inventory
    public boolean updateInventory(long updateAmount) throws SQLException
    {
      long currentAmount;
      long newAmount;

      Database.result = Database.statement.executeQuery("SELECT itemquantity FROM store_inventory WHERE (iditem = '" + itemID + "' AND idstore = '" + storeID + "')");
      if(Database.result.next())
      {
          currentAmount = Database.result.getLong(1);
          newAmount = (currentAmount + updateAmount);

          Database.statement.executeUpdate("UPDATE store_inventory SET itemquantity = '" + newAmount + "' WHERE (iditem = '" + itemID + "' AND idstore = '" + storeID + "')");
          return true;
      }
      else
      {
          return false;
      }
    }
     public static boolean verifyStoreInventory(int storeID, int itemID) throws SQLException
    {
    Database.result = Database.statement.executeQuery("SELECT iditem FROM store_inventory WHERE (iditem = '" + itemID + "' AND idstore = '" + storeID +"')"); //kind of redundent, but checks if the id exists

    if(Database.result.next())
    {
      return true;
    }

    else
    {
      return false;
    }
  }
   //Post: if the id exists, returns the quantity of that item in the store with storeID
   //	    else returns -1
    public int readInventory() throws SQLException
    {
      Database.result = Database.statement.executeQuery("SELECT itemquantity"
                  + " FROM store_inventory WHERE (iditem = '" + itemID +  "'AND idstore = '" + storeID +"')");

      if(Database.result.next())
      {
          return Database.result.getInt(1);
      }
      
      else
      {
          return -1;
      }
    }
    public boolean deleteInventory() throws SQLException
    {	 
	if (verifyStoreInventory(storeID, itemID))
	{
	    Database.statement.executeUpdate("DELETE FROM store_inventory WHERE idstore = '" + storeID + "' AND iditem = '" + itemID + "'");	
	    return true;
	}
	else
	{
	    return false;
	}
    }
    public static ArrayList<StoreInventory> readAllInventory(int id) throws SQLException
    {
	ArrayList<StoreInventory> list = new ArrayList();
	StoreInventory inventory;
	
	Database.result = Database.statement.executeQuery("SELECT iditem, itemquantity, defaultquantity, reorderlevel, reorderquantity"
		+ " FROM store_inventory WHERE (idstore = '" + id + "')");
	
	while (Database.result.next())
	{
	    inventory = new StoreInventory();
	    
	    inventory.setStoreID(id);
	    inventory.setItemID(Database.result.getInt(1));
	    inventory.setItemQuantity(Database.result.getInt(2));
	    inventory.setDefaultQuantity(Database.result.getInt(3));
            inventory.setReorderLevel(Database.result.getInt(4));
	    inventory.setReorderQuantity(Database.result.getInt(5));
	    
	    list.add(inventory);
	}
	
	return list;
    }
    public void setStoreID(int id)
    {
	storeID = id;
    }
    public void setItemID(int id)
    {
	itemID = id;
    }
    public void setItemQuantity(long num)
    {
	itemQuantity = num;
    }
    public void setDefaultQuantity(long num)
    {
	defaultQuantity = num;
    }
    public void setReorderLevel(long num)
    {
	reorderLevel = num;
    }
    public void setReorderQuantity(long num)
    {
	reorderQuantity = num;
    }
    public int getStoreID()
    {
	return storeID;
    }
    public int getItemID()
    {
	return itemID;
    }
    public long getItemQuantity()
    {
	return itemQuantity;
    }
    public long getDefaultQuantity()
    {
	return defaultQuantity;
    }
    public long getReorderLevel()
    {
	return reorderLevel;
    }
    public long getReorderQuantity()
    {
	return reorderQuantity;
    }
}