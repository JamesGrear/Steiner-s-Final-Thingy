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
public class Store 
{
    private static int currentStoreID;  //this is the ID of the store that the program is running for
    private static ArrayList<Store> stores; //all stores that exist
	    
    private int id;
    private int priority;
    private String address;
    private String city;
    private String state;
    private int zipcode;   
   

    public Store(int id)
    {
	this.id = id;
    }
    //Post: if the ID doesn't exist, adds a new store into the database and returns true
    //      if the ID already exists, returns false
    boolean registerNewStore() throws SQLException
    {
    if(!verifyStore(id))
	{
	    Database.statement.executeUpdate
				("INSERT INTO store(idstore, priority, address, city, state, zipcode)"
			        + "VALUES('" + id + "','" + priority + "','" + address + "','" + city + "','" + state + "','" + zipcode + "')");
	    return true;
	}
	else
	{
	    return false;
	}
    }
    //Post: if the ID exists, deletes the pre existing store from the database 
    //	    if the ID did not exist, returns false
    public boolean deleteStore() throws SQLException
    {
	if(verifyStore(id))
	{	    
	    Database.statement.executeUpdate("DELETE FROM store WHERE idstore = '" + id + "'");
			        
	    return true;
	}
	else
	{
	    return false;
	}
    }
    //Post: if the store exists, updates all attributes of a pre-existing store (except id)and returns true
    //	    if the store did not exist, returns false
    public boolean updateStore() throws SQLException
    {
		if(verifyStore(id))
		{
			Database.statement.executeUpdate("Update store Set address = '" + address + "' WHERE idstore = '" + id + "'");
			Database.statement.executeUpdate("Update store Set state = '" + state + "' WHERE idstore = '" + id + "'");
			Database.statement.executeUpdate("Update store Set priority = '" + priority + "' WHERE idstore = '" + id + "'");
			Database.statement.executeUpdate("Update store Set zipcode = '" + zipcode + "' WHERE idstore = '" + id + "'");
			Database.statement.executeUpdate("Update store Set city = '" + city + "' WHERE idstore = '" + id + "'");

			return true;
		}

		else
		{
			return false;
		}
    }
    //Post: if the id exists and newID doesn't, updates the id of a pre-existing store to newID in the database, updates object id to newID, returns true
    //	    else, returns false
    public boolean updateStoreID(int newID) throws SQLException
    {
		if(verifyStore(id))
		{
			if (!verifyStore(newID))
			{
				Database.statement.executeUpdate("UPDATE store SET idstore = '" + newID + "' WHERE idstore = '" + this.id + "'");
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
    //Pre : Static method, only called at class level. Returns an initialized item. Initializing item previously to calling this isn't necessary
    //	    i.e. Item item = Item.readItem(id);
    //Post: If an item with the passed in id exists in the database, an item object is returned with all fields set from database
    //	    else a null object is returned;
    public static Store readStore(int id) throws ClassNotFoundException, SQLException
    {
      	Item item;
	
	      if (verifyStore(id))
	      {
	        Database.result = Database.statement.executeQuery("SELECT address, city, state, priority, zipcode" + " FROM store WHERE (idstore = '" + id + "')");
	    
	        if (Database.result.next())
	        {
		        Store store = new Store(id);
		
		        store.address = Database.result.getString(1);
		        store.city = Database.result.getString(2);
		        store.state = Database.result.getString(3);
		        store.priority = Database.result.getInt(4);
		        store.zipcode = Database.result.getInt(5);	
		
            return store;
	        }
	      }
      
      return null; //if all else fails
    }
  
    // Pre: The id of the item has already been registered into the Item table
    //Post: if the item id exists in the item table but not the store_inventory table, the item and quantity are entered into the warehouse and returns true
    //	    else returns false
    public static boolean registerNewInventory(int storeID, int itemID, int quantity) throws ClassNotFoundException, SQLException
    {
      if(Item.verifyItem(itemID)) //item exists in item table
      {	
          if (!Store.verifyStoreInventory(storeID, itemID)) //item doesn't exist in warehouse table
          {
            Database.statement.executeUpdate("INSERT INTO store_inventory(idstore, iditem, itemquantity)"
                      + "VALUES('" + storeID + "','" + itemID + "','" + quantity + "')");
            
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
    public static boolean updateInventory(int storeID, int itemID, int updateAmount) throws SQLException
    {
      int currentAmount;
      int newAmount;

      Database.result = Database.statement.executeQuery("SELECT itemquantity FROM store_inventory WHERE (iditem = '" + itemID + "' AND idstore = '" + storeID + "')");
      if(Database.result.next())
      {
          currentAmount = Database.result.getInt(1);
          newAmount = (currentAmount + updateAmount);

          Database.statement.executeUpdate("UPDATE store_inventory SET itemquantity = '" + newAmount + "' WHERE (iditem = '" + itemID + "' AND idstore = '" + storeID + "')");
          return true;
      }
      else
      {
          return false;
      }
    }
  
   //Post: if the id exists, returns the quantity of that item in the store with storeID
   //	    else returns -1
    public static int readInventory(int storeID, int itemID) throws SQLException
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
  
  //Pre : This is a private static method. It is meant only for internal data verification
  //Post: Returns true if a store with the id exists, else returns false
  private static boolean verifyStore(int id) throws SQLException
  {
    Database.result = Database.statement.executeQuery("select idstore from store where idstore = '" + id + "'"); //kind of redundent, but checks if the id exists

    if(Database.result.next())
    {
        return true;
    }
  }

	//Pre : This is a private static method. It is meant only for internal data verification
	//Post: Returns true if a store with the id exists, else returns false
	private static boolean verifyStore(int id) throws SQLException
	{
		Database.result = Database.statement.executeQuery("select idstore from store where idstore = '" + id + "'"); //kind of redundent, but checks if the id exists

		if(Database.result.next())
		{
			return true;
		}
    
		else
		{
			return false;
		}
	}
  
 public static boolean verifyStoreInventory(int itemID, int storeID) throws SQLException
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
      
  public void setPriority(int priority)
	{
	  this.priority = priority;
	}
  
	public void setAddress(String address)
	{
	  this.address = address;
	}
  
	public void setCity(String city)
	{
	  this.city = city;
	}
  
	public void setState(String state)
	{
	  this.state = state;
	}
  
	public void setZipcode(int zipcode)
	{
	  this.zipcode = zipcode;
	}
  
	public static void setCurrentStoreID(int id)
	{
	  currentStoreID = id;
	}
  
	public int getID()
	{
	  return id;
	}
  
	public int getPriority()
	{
	  return priority;
	}
  
	public String getAddress()
	{
	  return address;
	}
  
	public String getCity()
	{
	  return city;
	}
  
	public String getState()
	{
	  return state;
	}
  
	public int getZipcode()
	{
	  return zipcode;
	}
  
	public static int getCurrentStoreID()
	{
	  return currentStoreID;
	}
}