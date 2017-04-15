/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Store;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.io.FileReader;

/**
 *
 * @author Brian
 */
public class BatchStoreUpdate extends BatchFileReader
{
    char[] storeID = new char[5];
    char[] address = new char[20];
    char[] city = new char[20];
    char[] state = new char[2];
    char[] zipcode = new char[9];
    char[] priority = new char[2];
    
    BatchStoreUpdate()
    {
	
	super();
	fileName = "adddeletestore.txt";
	error.writeHeader("ADD/DELETE STORE");
	
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
		    deleteStore(input);
		}
		else if(input.charAt(0) == 'A')
		{
		    int id = addStore(input);
		    
		    reader.mark(BUFFER_SIZE);
		    input = reader.readLine(); //mark your spot incase there are no I lines and no C line (that would be an error, but still)
		    
		    while(input != null && input.length() != 0 && input.charAt(0) == 'I') //for each Item line
		    {
			if (id != -1)
			{
			    addItem(input, id);
			}
			input = reader.readLine();
			rows++; //keep track of the I rows even if there was an error
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
	char[] chars;
	
	if(input.length() == 59) //if this isn't the length of the string, something is clearly wrong
	{
	    input = (new StringBuilder(input).deleteCharAt(0).toString()); //just deletes the first letter because we don't need it
	    
	    chars = input.toCharArray(); //break input into characters to read

	    //***********READS ALL THE CHARACTERS INTO THEIR RESPECTIVE VARIABLES*******
	    for(int i = 0; i < (input.length()); i++)
	    {
		if (i <= 4) //store id = 5 chars
		{
			storeID[i] = chars[i];
		}
		else if (i <= 24) //store id + address = 25 chars
		{
		    address[i - 5] = chars[i]; // subtract 5 to start at 0
		}
		else if (i <= 44) //previous + city = 45 chars
		{
		    city[i - 25] = chars[i]; // subtract 25 to start at 0
		}
		else if (i <= 46) //previous + state = 47 chars
		{
		    state[i-45] = chars[i];
		}
		else if (i <= 55) //previous + zipcode = 56 chars
		{
		    zipcode[i-47] = chars[i];
		}
		else if (i <= 57) //previous + priority = 58 chars
		{
		    priority[i-56] = chars[i];
		}			       
	    }
		//******DONE READING CHARACTERS INTO VARIABLES*******
		try
		{
		    int storeID2 = Integer.parseInt(new String(storeID));
		    int priority2 = Integer.parseInt(new String(priority)); //these could be non integer values, need to test them;
		    int zipcode2 = Integer.parseInt(new String(zipcode));
		    
		    Store store = new Store(storeID2);
		    
		    store.setAddress(new String(address));
		    store.setCity(new String(city));
		    store.setPriority(priority2);
		    store.setState(new String(state));
		    store.setZipcode(zipcode2);
		    
		    if (!store.registerNewStore()) //store already exists
		    {
			error.writeToLog("STORE WITH ID '" + Integer.parseInt(new String(storeID)) + "' ALREADY EXISTS");
		    }
		    else
		    {
			return storeID2;
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
    void addItem(String input, int id)
    {
	//write to file
	try
	{
	input = reader.readLine();
	System.out.println(input);
	}
	catch (Exception e)
	{
	    
	}
    }
    void deleteStore(String input) throws ClassNotFoundException, SQLException
    {
	rows++; //one row for a delete
	char[] chars;
	
	if(input.length() == 59) //if this isn't the length of the string, something is clearly wrong
	{
	    input = (new StringBuilder(input).deleteCharAt(0).toString()); //just deletes the first letter because we don't need it

	    chars = input.toCharArray(); //break input into characters to read

	    //***********READS ALL THE CHARACTERS INTO THEIR RESPECTIVE VARIABLES*******
	    for(int i = 0; i < (input.length()); i++)
	    {
		if (i <= 4) //store id = 5 chars
		{
			storeID[i] = chars[i];
		}
		else if (i <= 24) //store id + address = 25 chars
		{
		    address[i - 5] = chars[i]; // subtract 5 to start at 0
		}
		else if (i <= 44) //previous + city = 45 chars
		{
		    city[i - 25] = chars[i]; // subtract 25 to start at 0
		}
		else if (i <= 46) //previous + state = 47 chars
		{
		    state[i-45] = chars[i];
		}
		else if (i <= 55) //previous + zipcode = 56 chars
		{
		    zipcode[i-47] = chars[i];
		}
		else if (i <= 57) //previous + priority = 58 chars
		{
		    priority[i-56] = chars[i];
		}
	    }
		//******DONE READING CHARACTERS INTO VARIABLES*******
		
			
	    Store store = Store.readStore(Integer.parseInt(new String(storeID))); //this is the supposed store we want to delete
			
	    if(store != null)
	    {
		String dbAddress = String.format("%-20s", store.getAddress()); //formatting for db values so they will match file values
		String dbCity = String.format("%-20s", store.getCity());	//(pads left with spaces)
		
		try
		{
		    int priority2 = Integer.parseInt(new String(priority)); //these could be non integer values, need to test them;
		    int zipcode2 = Integer.parseInt(new String(zipcode));
		    
		    //********COMPARING FILE DATA TO DATABASE************
		    if (dbAddress.equals(new String(address)) //checking to make sure all attributes match with database
		    && dbCity.equals(new String(city)) 
		    && store.getState().equals(new String(state))
		    && store.getPriority() == priority2
		    && store.getZipcode() == zipcode2) //look at how much fun we're having with these 5 comparisons!
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
}
