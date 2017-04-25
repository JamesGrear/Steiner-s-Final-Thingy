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
import Database.Warehouse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class BatchInventoryToStore extends BatchFileReader
{
    private int trailerCount;
    private final int MAX_PRIORITY = 15;
    private PrintWriter writer;
    
    BatchInventoryToStore()
    {
	super();
	fileName = "fullstoreupdate.txt";
	trailerCount = 0;
	error.writeHeader("INVENTORY TO STORE");
	
	try
	{
	    writer = new PrintWriter(fileName);
	    reader = new BufferedReader(new FileReader(fileName));
	    sequenceNumber = FileSequence.readInventoryToStore();
	}
	catch(FileNotFoundException e)
	{
	    error.writeToLog(fileName + " FILE NOT FOUND");
	    fileNotFound = true;
	}
	catch (SQLException e)
	{
	    error.writeToLog("DATABASE ERROR. CHECK YOUR DATABASE AND TRY AGAIN.");
	    fileNotFound = true; //not strictly true, but cancels the file reading
	}
    }
   
    public boolean readFile()
    {
	String input;
	
	combineFiles();
	
	if(fileNotFound)
	{
	    return false;
	}
	//**************************************************************
	//******************READ THE HEADER*****************************
	//**************************************************************
	if (!readHeader())
	{
	    System.out.println("Failed to read the Header");
	    return false;
	}
	//**************************************************************
	//******************READ THE CONTENT****************************
	//**************************************************************
	try
	{
	    String priority;
	    
	    for (int i = 0; i < MAX_PRIORITY; i++) //runs once for each priority, starting with 1 and ending with 15
	    {
		reader = new BufferedReader(new FileReader(fileName));
		
		reader.mark(BUFFER_SIZE);
		input = reader.readLine();
		
		while (input != null && input.length() != 0 && input.charAt(0) != 'T')
		{
		    if(input.charAt(0) == 'A' || input.charAt(0) == 'O' || input.charAt(0) == 'B')
		    {
			priority = input.substring(6, 8);
			
			try
			{
			    if(Integer.parseInt(priority) == i) //store matches the priority we're currently checking
			    {
				addItem(input);
				rows++;
			    }
			}
			catch(IllegalArgumentException e)
			{
			    error.writeToLog("INCORRECT FORMATTING FOR ADD ITEM TO STORE");
			}
		    }
		    reader.mark(BUFFER_SIZE); //mark your spot so you dont skip over the Trailer
		    input = reader.readLine();
		}
		reader.reset();
	    }
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
    private void addItem(String input)
    {
	String storeID;
	String priority;
	String itemID;
	String quantity;
	int storeID2;
	int priority2;
	int itemID2;
	long quantity2;
	long warehouseInventory;
	StoreInventory storeInventory;
	
	if(input.length() != 27)
	{
	    error.writeToLog("INCORRECT FORMATTING FOR ADD ITEM TO STORE");
	    return;
	}
	
	storeID = input.substring(1, 6);
	priority = input.substring(6, 8);
	itemID = input.substring(8, 17);
	quantity = input.substring(17, 27);
	
	try
	{
	    storeID2 = Integer.parseInt(storeID);
	    priority2 = Integer.parseInt(priority);
	    itemID2 = Integer.parseInt(itemID);
	    quantity2 = Long.parseLong(quantity);
	    
	   //makess sure the item exists.
	    if(Item.verifyItem(itemID2))
	    {
		//makess sure the inventory exists in the warehouse
		if(Warehouse.verifyWarehouseInventory(itemID2))
		{
		    //makes sure the store exists
		    if(Store.verifyStore(storeID2))
		    {
			warehouseInventory = Warehouse.readInventory(itemID2);
			
			//make sure warehouse inventory isn't empty
			if(warehouseInventory != -1)
			{
			    storeInventory = new StoreInventory();
			    storeInventory.setItemID(itemID2);
			    storeInventory.setStoreID(storeID2);
			    
			    //make sure warehouse has enough inventory to satisfy request
			    if(warehouseInventory <= quantity2)
			    {
				storeInventory.updateInventory(quantity2); //add inventory to store
				Warehouse.updateInventory(itemID2, (quantity2 * -1)); //remove inventory from warehouse
			    }
			    else //not enough inventory, give as much as we can
			    {
				storeInventory.updateInventory(warehouseInventory); //add remaining warehouse inventory to store
				Warehouse.updateInventory(itemID2, (warehouseInventory * -1)); //remove remaining warehouse inventory
				error.writeToLog("WAREHOUSE INVENTORY TOO LOW. STORE #" + storeID2 + " REQUESTED " + quantity2 + "OF ITEM #" + itemID2 + " BUT GOT " + warehouseInventory);
			    }
			}
			else
			{
			    error.writeToLog("WAREHOUSE INVENTORY IS EMPTY FOR ITEM #" + itemID2);
			}
		    }
		    else
		    {
			error.writeToLog("STORE #" + storeID2 + " DOESN'T EXIST");
		    }
		}
		else
		{
		    error.writeToLog("WAREHOUSE INVENTORY DOESN'T EXIST FOR ITEM #" + itemID2);
		}
	    }
	    else
	    {
		error.writeToLog("ITEM #" + itemID2 + " DOESN'T EXIST");
	    }
	}
	catch(IllegalArgumentException e)
	{
	    error.writeToLog("INCORRECT FORMATTING FOR ADD ITEM TO STORE");
	}
	catch(SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
    private void combineFiles()
    {
	File file;
	  
	writeHeader();
	
	file = new File("newstores.txt");
	writeFile('A', file, writer);
	    
	file = new File("onlinestorerequest.txt");
	writeFile('O', file, writer);
	    
	file = new File("storeupdate.txt");
	writeFile('B', file, writer);
	
	writeTrailer();
    }
    private void writeFile(char c, File file, PrintWriter writer)
    {
	int counter = 0;
	String input; //used with reader2
	BufferedReader reader2; //reads in the file to be combined
	
	try
	{
	    reader2 = new BufferedReader(new FileReader(file)); //reads the passed in file
	    
	    if(!readHeader(reader2))
	    {
		error.writeToLog("COULD NOT READ HEADER FOR " + file);
		return;
	    }
	    
	    reader2.mark(BUFFER_SIZE);
	    input = reader2.readLine();
	   
	    while (input != null && input.length() != 0 && input.charAt(0) != 'T')
	    {
		if(input.charAt(0) == c)
		{
		    writer.println(input);
		    counter++;
		    trailerCount++;
		}
		writer.flush();
		reader2.mark(BUFFER_SIZE); //mark your spot so you dont skip over the Trailer
		input = reader2.readLine();
	    }
	    reader2.reset();
	    
	    if(!readTrailer(reader2, counter))
	    {
		error.writeToLog("TRAILER FOR " + file + " IS INCORRECT");
	    }
	}
	catch(FileNotFoundException e)
	{
	    error.writeToLog(file + " IS MISSING");
	}
	catch(IOException e)
	{
	    error.writeToLog("UNKNOWN IO ERROR");
	}
    }
    //Overrides readHeader, only difference is this method uses the reader passed in instead of the one the class provides
    private boolean readHeader(BufferedReader reader)
    {
	String expected;
	String[] line;
	StringBuilder input = new StringBuilder();
	
	expected = "HD " + String.format("%04d", sequenceNumber);
	
	try
	{
	    line = reader.readLine().split(" "); //splits the line up into words seperated by a space
	    
	    input.append(line[0]) //appends the first and second word, putting the space back in
		 .append(" ")
	         .append(line[1]);
	}
	catch(NullPointerException e)
	{
	    error.writeToLog("HEADER DOES NOT EXIST");
	    return false;
	}
	catch(IOException e)
	{
	    error.writeToLog("FAILED TO READ HEADER");
	    return false;
	}
	
	System.out.println(input);
	System.out.println(expected);
	
	if (expected.equals(input.toString())) //only first 2 words of the header are read, the rest is ignored
	{
	    return true;
	}
	else
	{
	    System.out.println("Failed to read the Header");
	    return false;
	}
    }
    //Overrides readTrailer, only difference is this method uses the int passed in to check the trailer
    private boolean readTrailer(BufferedReader reader, int rows)
    {
	String expected;
	String input = "";
	
	expected = "T " + String.format("%04d", rows);
	
	try
	{
	    input = reader.readLine();
	}
	catch (IOException e)
	{
	    error.writeToLog("FAILED TO READ TRAILER");
	}
	
	if(input != null)
	{
	    if (expected.equals(input))
	    {
	     return true;
	    }
	    else
	    {
		return false;
	    }
	}
	else
	{
	    error.writeToLog("TRAILER LINE EMPTY");
	    return false;
	}
    }
    private void writeHeader()
    {
	writer.println("HD " + String.format("%04d", sequenceNumber));
	writer.flush();
    }
    private void writeTrailer()
    {
	writer.println("T " + String.format("%04d", trailerCount));
	writer.flush();
    }
}