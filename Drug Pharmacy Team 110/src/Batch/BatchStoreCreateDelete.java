/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Item;
import Database.Store;
import Database.StoreInventory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Brian
 */
public class BatchStoreCreateDelete extends BatchFileReader
{
    String storeID;
    String address;
    String city;
    String state;
    String zipcode;
    String priority;
    File outputFile;
    File deletedInventory;
    PrintWriter writer;
    PrintWriter writer2;
    
    BatchStoreCreateDelete() throws FileNotFoundException
    {
	
	super();
	fileName = "adddeletestore.txt";
	error.writeHeader("ADD/DELETE STORE");
	outputFile = new File("newstores.txt");
	deletedInventory = new File("deletedstores.txt");
	
	try
	{
	    reader = new BufferedReader(new FileReader(fileName));
	}
	catch(FileNotFoundException e)
	{
	    error.writeToLog(fileName + " FILE NOT FOUND");
	    fileNotFound = true;
	}
	try
	{
	    writer = new PrintWriter(outputFile);
	    writer2 = new PrintWriter(deletedInventory);
	} 
	catch (IOException e) 
	{
	    
	}
	try
	{
	    sequenceNumber = FileSequence.readStoreCreateDelete();
	}
	catch (SQLException e)
	{
	    error.writeToLog("DATABASE ERROR. CHECK YOUR DATABASE AND TRY AGAIN.");
	    fileNotFound = true; //not strictly true, but cancels the file reading
	}
    }
    
    boolean ReadFile() throws ClassNotFoundException, SQLException
    { 
	String input;
	
	if(fileNotFound)
	{
	    return false;
	}
	//**************************************************************
	//******************READ THE HEADER*****************************
	//**************************************************************
	if (readHeader())
	{
	    System.out.println("Successfully read the Header");
	}
	//**************************************************************
	//******************READ THE CONTENT****************************
	//**************************************************************
	try
	{
	    reader.mark(BUFFER_SIZE);
	    input = reader.readLine();
	   
	    while (input != null && input.length() != 0 && input.charAt(0) != 'T')
	    {
		if(input.charAt(0) == 'D')
		{
		    deleteStore(input); //removes store from database
		    reallocateStoreInventory(Integer.parseInt(storeID)); //reads inventory to file and deletes inventory from database
		}
		else if(input.charAt(0) == 'A')
		{
		    int id = addStore(input);
		    
		    reader.mark(BUFFER_SIZE);
		    input = reader.readLine(); //mark your spot incase there are no I lines and no C line (that would be an error, but still)
		    
		    if(input != null && input.charAt(0) == 'I')
		    {
			while(input != null && input.length() != 0 && input.charAt(0) == 'I') //for each Item line
			{
			 
			    if (id != -1)
			    {
				addItem(input, id);
			    }
			    rows++; //keep track of the I rows even if there was an error
			    input = reader.readLine();
			}
		    }
		    else
		    {
			error.writeToLog("NO ITEM LINES FOUND FOR STORE WITH ID '" + id + "'");
		    }
		}
		reader.mark(BUFFER_SIZE); //mark your spot so you dont skip over the Trailer
		input = reader.readLine();
	    }
	    reader.reset();
	}
	catch(IOException e)
	{
	    
	}
	//**************************************************************
	//******************READ THE TRAILER****************************
	//**************************************************************
	if (readTrailer())
	{
	    System.out.println("Successfully read the Trailer");
	}
	
	return true;
    }
    int addStore(String input)
    {
	rows++; //first line of add
	
	if(input.length() == 59) //if this isn't the length of the string, something is clearly wrong
	{
	    storeID = input.substring(1, 6);
	    address = input.substring(6, 26);
	    city = input.substring(26, 46);
	    state = input.substring(46, 48);
	    zipcode = input.substring(48, 57);
	    priority = input.substring(57, 59);
	    
		try
		{  
		    Store store = new Store(Integer.parseInt(storeID));
		    
		    store.setAddress(address);
		    store.setCity(city);
		    store.setPriority(Integer.parseInt(priority));
		    store.setState(state);
		    store.setZipcode(Integer.parseInt(zipcode));
		    
		    if (!store.registerNewStore()) //store already exists
		    {
			error.writeToLog("STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "' ALREADY EXISTS");
		    }
		    else
		    {
			return Integer.parseInt(storeID);
		    }
		    
		}
		catch(Exception e) //bad formating/values in file
		{
		    error.writeToLog("INCORRECT VALUES FOR ADD STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "'");
		}
	}
	else //bad file format
	{
	    error.writeToLog("INCORRECT FORMATTING");
	}

	return -1; //something went wrong, can't process further;
    }
    void addItem(String input, int id) throws ClassNotFoundException, SQLException //writes to file "newstores.txt" to be read during InventoryToStore process
    {
	StoreInventory inventory = new StoreInventory();
	int itemId;
	long defaultQuantity;
	long reorderLevel;
	long reorderQuantity;
	
	if(input.length() != 40) //line not formatted correctly
	{
	    error.writeToLog("ITEM FOR STORE WITH ID '" + id + "' IS NOT INCORRECT"); 
	    return;
	}
	
	itemId = Integer.parseInt(input.substring(1, 10)); //get the id from the string
	defaultQuantity = Long.parseLong(input.substring(10, 20)); //get defaultQuantity from the string
	reorderLevel = Long.parseLong(input.substring(20, 30)); //get reorderLevel from the string
	reorderQuantity = Long.parseLong(input.substring(30, 40)); //get reorderQuantity from the string
	
	if(!Item.verifyItem(itemId)) //item doesn't exist
	{
	    error.writeToLog("ITEM WITH ID '" + itemId + "' DOES NOT EXIST");
	    return;
	}
	
	inventory.setItemQuantity(0); //no items exist in new store
	inventory.setItemID(itemId);
	inventory.setStoreID(id);
	inventory.setDefaultQuantity(defaultQuantity);
	inventory.setReorderQuantity(reorderQuantity);
	inventory.setReorderLevel(reorderLevel);
	
	if(!inventory.registerNewInventory())
	{
	    error.writeToLog("FAILED TO CREATE INVENTORY FOR STORE #" + id);
	}
	
	writer.println("A" + storeID + priority + input.substring(1, 10) + input.substring(10, 20)); //write to file for reading later
	writer.flush();
    }
    void deleteStore(String input) throws ClassNotFoundException, SQLException
    {
	rows++; //one row for a delete
	
	if(input.length() == 59) //if this isn't the length of the string, something is clearly wrong
	{
	    storeID = input.substring(1, 6);
	    address = input.substring(6, 26);
	    city = input.substring(26, 46);
	    state = input.substring(46, 48);
	    zipcode = input.substring(48, 57);
	    priority = input.substring(57, 59);
		
	    Store store = Store.readStore(Integer.parseInt(storeID)); //this is the supposed store we want to delete
			
	    if(store != null)
	    {
		String dbAddress = String.format("%-20s", store.getAddress()); //formatting for db values so they will match file values
		String dbCity = String.format("%-20s", store.getCity());	//(pads left with spaces)
		
		try
		{
		    if(Integer.parseInt(priority) > 15 || Integer.parseInt(priority) < 0) //values out of range
		    {
			throw new Exception();
		    }
		    
		    //********COMPARING FILE DATA TO DATABASE************
		    if (dbAddress.equals(address) //checking to make sure all attributes match with database
		    && dbCity.equals(city) 
		    && store.getState().equals(state)
		    && store.getPriority() == Integer.parseInt(priority)
		    && store.getZipcode() == Integer.parseInt(zipcode)) //look at how much fun we're having with these 5 comparisons!
		    {
			System.out.println("Store deleted");
			store.deleteStore();
		    }
		    else //Store doesn't exist, can't be deleted
		    {
			error.writeToLog("MISMATCHED DATA WITH DATABASE FOR DELETE STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "'");
		    }
		}
		catch(Exception e) //bad formating in file
		{
		    error.writeToLog("INCORRECT VALUES FOR DELETE STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "'");
		}
	    }
	    else //store doesn't exist, can't be deleted
	    {
		error.writeToLog("STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "' DOESN'T EXIST AND CAN'T BE DELETED");
	    }
	    //********DONE COMPARING FILE DATA TO DATABASE************
	}
	else //formatting of line was wrong, can't read it
	{
	    error.writeToLog("INCORRECT FORMATTING");
	}
    }
    void reallocateStoreInventory(int id) throws SQLException //for deleted stores
    {
	//try
	{
	    ArrayList<StoreInventory> deletedStores = StoreInventory.readAllInventory(id);
	    
	    for(StoreInventory x : deletedStores)
	    {
		String storeid = String.format("%09d", x.getItemID());
		String quantity = String.format("%010d", x.getItemQuantity());
		String expiration = "0000-00-00"; //not relevant for our program
		writer2.println(storeid + quantity + expiration);
		writer2.flush();
		
		x.deleteInventory();
	    }
	}
	//catch(Exception e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
}
