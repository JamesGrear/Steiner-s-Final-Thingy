/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Brian
 */
public abstract class BatchFileReader 
{
    protected final int BUFFER_SIZE = 1000;
    protected boolean fileNotFound;
    protected String fileName;
    protected BufferedReader reader;
    protected ErrorReport error;
    protected int sequenceNumber;
    protected int rows;
    
    BatchFileReader()
    {
	rows = 0;
	error = ErrorReport.getErrorReport();
    }
    public boolean readHeader()
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
	}
	catch(Exception e)
	{
	    error.writeToLog("FAILED TO READ HEADER");
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
    public boolean readTrailer()
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
	    System.out.println(input);
	    System.out.println(expected);
	    if (expected.equals(input))
	    {
	     return true;
	    }
	    else
	    {
		error.writeToLog("INCORRECT TRAILER");
		return false;
	    }
	}
	else
	{
	    error.writeToLog("TRAILER LINE EMPTY");
	    return false;
	}
    }
}
