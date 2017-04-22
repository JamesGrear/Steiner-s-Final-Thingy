/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class BatchInventoryToStore extends BatchFileReader
{
    BatchInventoryToStore()
    {
	super();
	//fileName = "adddeletestore.txt";
	error.writeHeader("INVENTORY TO STORE");
	
	try
	{
	    reader = new BufferedReader(new FileReader(fileName));
	    sequenceNumber = FileSequence.readStoreCreateDelete();
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
    
    void combineFiles()
    {
	
    }
}
