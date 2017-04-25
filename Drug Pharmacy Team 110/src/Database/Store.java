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
    public boolean registerNewStore() throws SQLException
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