/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Item;
import Database.Warehouse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Brian
 */
public class BatchVendorInventoryRequest 
{
    private File file;
    private PrintWriter writer;
    private ErrorReport error;
    private int sequence;
    private int rows;
    
    BatchVendorInventoryRequest()
    {
	error = ErrorReport.getErrorReport();
	error.writeHeader("VENDOR INVENTORY REQUEST");
	rows = 0;
	
	try
	{
	    file = new File("vendororder.txt");
	    writer = new PrintWriter(file);
	    sequence = FileSequence.readInventoryOrder();
	}
	catch(FileNotFoundException e)
	{
	    error.writeToLog(file + " not found");
	}
	catch(SQLException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
    
    public void writeFile()
    {
	String vendorCode;
	String itemID;
	String quantity;
	ArrayList<Warehouse> inventory = new ArrayList<>();
	Item item;
	
	writeHeader();
	
	try
	{
	    inventory = Warehouse.readAllInventory();
	    
	    for(Warehouse x: inventory)
	    {
		item = Item.readItem(x.getItemID());  //create an item with the id from warehouse
		
		if(item != null)
		{
		    if(x.getItemQuantity() <= item.getReorderLevel()) //need to order more of this item for the warehouse
		    {
			vendorCode = String.format("%04d", x.getVendorCode());
			itemID = String.format("%09d", x.getItemID());
			quantity = String.format("%010d", item.getReorderQuantity());
			 
			writer.println(vendorCode + itemID + quantity); //write out the formatted line
			writer.flush();
		    }
		}
		else //item doesn't exist
		{
		    error.writeToLog("Item #" + x.getItemID() + " DOES NOT EXIST IN ITEM TABLE");
		}
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
	    
	writeTrailer();
    }
    private void writeHeader()
    {
	Date date = new Date();
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	writer.println("HD " + String.format("%04d", sequence) + "      " + format.format(date));
	writer.flush();
    }
    private void writeTrailer()
    {
	writer.println("T " + String.format("%04d", rows));
	writer.flush();
    }
}