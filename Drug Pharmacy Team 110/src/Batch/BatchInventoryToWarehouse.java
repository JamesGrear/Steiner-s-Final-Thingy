/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Item;
import Database.Warehouse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class BatchInventoryToWarehouse extends BatchFileReader
{
    String vendorCode;
    String itemID;
    String quantityReceived;
    String expiration;
    
    BatchInventoryToWarehouse()
    {
	super();
	fileName = "itemreceived.txt";
	error.writeHeader("INVENTORY TO WAREHOUSE");
	
	try
	{
	    reader = new BufferedReader(new FileReader(fileName));
	    sequenceNumber = FileSequence.readInventoryToWarehouse();
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
    public void readFile()
    {
	privateReadFile(); //read external file
	fileNotFound = false; //reset for next file
	
	try
	{
	    reader = new BufferedReader(new FileReader("deletedstores.txt"));
	    privateReadFile();
	}
	catch(FileNotFoundException e)
	{
	    error.writeToLog("deletedstores.txt FILE NOT FOUND");
	    fileNotFound = true;
	}
    }
    private void privateReadFile() //private version that is read by the public method
    { 
	String input;
	
	if(fileNotFound)
	{
	    return;
	}
	//**************************************************************
	//******************READ THE HEADER*****************************
	//**************************************************************
	if (!readHeader())
	{
	    System.out.println("Failed to read the Header");
	    return;
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
		addItem(input);
		reader.mark(BUFFER_SIZE); //mark your spot so you dont skip over the Trailer
		input = reader.readLine();
		rows++;
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
    }
    private void addItem(String input)
    {
	int vendor = 0;
	int item = 0;
	long quantity = 0;
	
	if (input.length() != 33)
	{
	    error.writeToLog("INCORRECT ITEM FORMAT");
	    return;
	}
	
	try
	{
	    vendor = Integer.parseInt(input.substring(0, 4));
	    item = Integer.parseInt(input.substring(4, 13));
	    quantity = Long.parseLong(input.substring(13, 23));
	    expiration = input.substring(23, 33); //we don't really care about this
	}
	catch(Exception e)
	{
	    error.writeToLog("INCORRECT ITEM FORMAT");
	    return;
	}
	
	try
	{
	    if (Item.verifyItem(item) && Warehouse.verifyWarehouseInventory(item)) //item exists 
	    {
		Warehouse.updateInventory(item, quantity);
	    }
	    else
	    {
		error.writeToLog("ITEM WITH ID #" + item + " DOESN'T EXIST");
	    }
	}
	catch(SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
}