/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Item;
import Database.StoreInventory;
import Database.Warehouse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Brian
 */
public class BatchItemUpdate extends BatchFileReader
{
    private int rowA;
    private int rowD;
    private int rowC;
    
    BatchItemUpdate()
    {
	super();
	fileName = "items.txt";
	error.writeHeader("ITEM UPDATE");
	
	rowA = rowD = rowC = 0;
	
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
	    sequenceNumber = FileSequence.readItemUpdate();
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
	    reader.mark(BUFFER_SIZE);
	    input = reader.readLine();
	   
	    while (input != null && input.length() != 0 && input.charAt(0) != 'T')
	    {
		while(input != null && input.length() != 0 && input.charAt(0) == 'D') //delete item
		{
		    deleteItem(input);
		    rows++;
		    rowD++;
		    
		    reader.mark(BUFFER_SIZE);
		    input = reader.readLine();
		    
		    if(input != null && input.length() != 0 && input.charAt(0) == 'N')
		    {
			if(input.charAt(1) != 'D')
			{
			    error.writeToLog("INCORRECT FORMAT FOR DELETE COUNT LINE");
			}
			
			try
			{
			    if(rowD != Integer.parseInt(input.substring(3, 7)))
			    {
				error.writeToLog("DELETE COUNT DOESN'T MATCH FILE");
			    }
			}
			catch(Exception e)
			{
			    error.writeToLog("INCORRECT FORMAT FOR DELETE COUNT LINE");
			}
		    }
		}
		while(input != null && input.length() != 0 && input.charAt(0) == 'C') //change item
		{
		    changeItem(input);
		    rows++;
		    rowC++;
		    
		    reader.mark(BUFFER_SIZE);
		    input = reader.readLine();
		    
		    if(input != null && input.length() != 0 && input.charAt(0) == 'N')
		    {
			if(input.charAt(1) != 'C')
			{
			    error.writeToLog("INCORRECT FORMAT FOR CHANGE COUNT LINE");
			}
			
			try
			{
			    if(rowC != Integer.parseInt(input.substring(3, 7)))
			    {
				error.writeToLog("CHANGE COUNT DOESN'T MATCH FILE");
			    }
			}
			catch(Exception e)
			{
			    error.writeToLog("INCORRECT FORMAT FOR CHANGE COUNT LINE");
			}
		    }
		}
		while(input != null && input.length() != 0 && input.charAt(0) == 'A') //change item
		{
		    addItem(input);
		    rows++;
		    rowA++;
		    
		    reader.mark(BUFFER_SIZE);
		    input = reader.readLine();
		    
		    if(input != null && input.length() != 0 && input.charAt(0) == 'N')
		    {
			if(input.charAt(1) != 'A')
			{
			    error.writeToLog("INCORRECT FORMAT FOR ADD COUNT LINE");
			}
			
			try
			{
			    if(rowA != Integer.parseInt(input.substring(3, 7)))
			    {
				error.writeToLog("ADD COUNT DOESN'T MATCH FILE");
			    }
			}
			catch(Exception e)
			{
			    error.writeToLog("INCORRECT FORMAT FOR ADD COUNT LINE");
			}
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
    private void addItem(String input)
    {
	Item item;
	
	String id;
	String name;
	String description;
	String dosage;
	String reorderLevel;
	String vendorCode;
	String reorderQuantity;
	String deliveryTime;
	
	int length = input.length();
	
	try
	{
	    id = input.substring(1, 10);
	    name = input.substring(10, 30);
	    description = input.substring(30, 130);
	    dosage = input.substring(130, 150);
	    reorderLevel = input.substring(150, 160);  
	    vendorCode = input.substring(160, 164);
	    reorderQuantity = input.substring(164, 174);
	    deliveryTime = input.substring(174, length);    
	}
	catch(IndexOutOfBoundsException e)
	{
	    error.writeToLog("INCORRECT FORMAT FOR ADD ITEM LINE");
	    return;
	}
	
	try
	{
	    item = new Item(Integer.parseInt(id));
	    item.setCost(5); //default cost for all items, change online if needed
	    item.setName(name);
	    item.setDescription(description);
	    item.setDosage(dosage);
	    item.setReorderLevel(Long.parseLong(reorderLevel));
	    item.setVendorCode(Integer.parseInt(vendorCode));
	    item.setReorderQuantity(Long.parseLong(reorderQuantity));
	    item.setDeliveryTime(deliveryTime);
	    
	    Warehouse.registerNewInventory(item.getID(), item.getVendorCode(), 0); //add to warehouse but give a quantity of 0

	    
	    if(!item.registerNewItem())
	    {
		error.writeToLog("ITEM #" + id + " ALREADY EXISTS");
	    }
	}
	catch(ClassNotFoundException e)//|SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
	catch(Exception e)
	{
	    error.writeToLog("INCORRECT FORMAT FOR ADD ITEM LINE");
	}
    }
    private void deleteItem(String input)
    {
	boolean isEmpty = true;
	Item item;
	ArrayList<StoreInventory> inventories;
	int id;
	
	if(input.length() != 10)
	{
	    error.writeToLog("INCORRECT FORMAT FOR DELETE ITEM LINE");
	}
	
	try
	{
	    id = Integer.parseInt(input.substring(1,10));
	    
	    item = new Item(id);
	    inventories = StoreInventory.readAllInventory(id);
	    
	    if(Warehouse.readInventory(id) > 0) //check warehouse for inventory
	    {
		isEmpty = false;
	    }
	}
	catch(IllegalArgumentException e)
	{
	    error.writeToLog("INCORRECT FORMAT FOR DELETE ITEM LINE");
	    return;
	}
	catch(ClassNotFoundException | SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	    return;
	}
	
	if(isEmpty) //no point checking if warehouse wasn't empty
	{
	    for(StoreInventory x: inventories) 
	    {
		if(x.getItemQuantity() > 0) //checks each store for invenotry;
		{
		    isEmpty = false;
		}
	    }
	}
	
	try
	{
	    if(isEmpty)
	    {
		if(!item.deleteItem()) //goodbye, item!
		{
		    error.writeToLog("CANNOT DELETE ITEM #" + id + ". ITEM DOES NOT EXIST");
		}
	    }
	    else
	    {
		error.writeToLog("CANNOT DELETE ITEM #" + id + ", INVENTORY STILL EXISTS");
	    }
	}
	catch(SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
    private void changeItem(String input)
    {
	Item item; 
	
	String id;
	String name;
	String description;
	String dosage;
	String reorderLevel;
	String vendorCode;
	String reorderQuantity;
	String deliveryTime;
	
	int length = input.length();
	
	try
	{
	    id = input.substring(1, 10);
	    name = input.substring(10, 30);
	    description = input.substring(30, 130);
	    dosage = input.substring(130, 150);
	    reorderLevel = input.substring(150, 160);  
	    vendorCode = input.substring(160, 164);
	    reorderQuantity = input.substring(164, 174);
	    deliveryTime = input.substring(174, length);    
	}
	catch(IndexOutOfBoundsException e)
	{
	    error.writeToLog("INCORRECT FORMAT FOR CHANGE ITEM LINE");
	    return;
	}
	
	try
	{
	    item = new Item(Integer.parseInt(id));
	    item.setCost(5); //default cost for all items, change online if needed
	    item.setName(name);
	    item.setDescription(description);
	    item.setDosage(dosage);
	    item.setReorderLevel(Long.parseLong(reorderLevel));
	    item.setVendorCode(Integer.parseInt(vendorCode));
	    item.setReorderQuantity(Long.parseLong(reorderQuantity));
	    item.setDeliveryTime(deliveryTime);

	    if(!item.updateItem())
	    {
		error.writeToLog("ITEM #" + id + " DOESN'T EXIST AND CAN'T BE CHANGED");
	    }
	}
	catch(ClassNotFoundException e)//|SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
	catch(Exception e)
	{
	    error.writeToLog("INCORRECT FORMAT FOR ADD ITEM LINE");
	}
    }
}