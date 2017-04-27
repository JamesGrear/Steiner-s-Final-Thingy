/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.FileSequence;
import Database.Sales;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Brian
 */
public class BatchCalculateSalesReport extends BatchFileReader
{
    private PrintWriter writer;
    private String reportName;
    private Date date;
    private DateFormat format;
    
    BatchCalculateSalesReport()
    {
		super();
		date = new Date();
		format = new SimpleDateFormat("MM-dd-yyy");

		fileName = "reports.txt";
		reportName = "Yearly Sales Report " + format.format(date) + ".txt";
		error.writeHeader("YEARLY REPORTS");

		try
		{
			writer = new PrintWriter(reportName);
			reader = new BufferedReader(new FileReader(fileName));
			sequenceNumber = FileSequence.readYearlySales();
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
		String input;
		int id;

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
				if(input.charAt(0) == 'I')
				{
					try
					{
						if(input.length() == 10)
						{
							id = Integer.parseInt(input.substring(1, 10));

							writeReport(id);
						}
						else
						{
							error.writeToLog("INCORRECT FORMAT FOR YEARLY REPORT REQUEST");
						}
					}

					catch(IllegalArgumentException e)
					{
						error.writeToLog("INCORRECT FORMAT FOR YEARLY REPORT REQUEST");
					}

					rows++;
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
    }
    private void writeReport(int id)
    {
	double total = 0;
	double yearlyTotal = 0;
	int month = 0;
	Calendar start = Calendar.getInstance();
	Calendar end = Calendar.getInstance();
	int years;
	ArrayList<Sales> sales;
	
	try
	{
	    sales = Sales.readAllSales(id);
	    
	    writer.println("                    YEARLY SALES FOR ITEM #" + id + "\n\n");
	    writer.println();
	    
	    if(sales.isEmpty()) //nothing to report on
	    {
		return;
	    }
	    
	    start.setTime(sales.get(0).getDate()); //earliest date of sales
	    end.setTime(sales.get(sales.size() - 1).getDate()); //last date of sales
	    
	    years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR) + 1;
	    
	    for(int i = 0; i < years; i++) //for each year the item was sold
	    {
		writer.print(String.format("%15s", "STORE ID")); //pad with spaces
		writer.print(String.format("%30s", "QUANTITY SOLD"));
		writer.print(String.format("%30s", "TOTAL PRICE"));
		writer.println();
		
		for(Sales x: sales) //for each sale of the item
		{
		    Calendar year = Calendar.getInstance();
		    year.setTime(x.getDate());
		    
		    if(year.get(Calendar.YEAR) == (start.get(Calendar.YEAR) - i)) //if we're reporting on the right year
		    {
			if (year.get(Calendar.MONTH) != month) //if we're reporting on a new month
			{
			    if (year.get(Calendar.MONTH) == 0)
			    {
				writer.println();
				writer.println(String.format("%50s", "JANUARY"));
				month = 0;
			    }
			    if (year.get(Calendar.MONTH) == 1)
			    {
				writer.println();
				writer.println(String.format("%50s", "FEBRUARY"));
				month = 1;
			    }
			    if (year.get(Calendar.MONTH) == 2)
			    {
				writer.println();
				writer.println(String.format("%50s", "MARCH"));
				month = 2;
			    }
			    if (year.get(Calendar.MONTH) == 3)
			    {
				writer.println();
				writer.println(String.format("%50s", "APRIL"));
				month = 3;
			    }
			    if (year.get(Calendar.MONTH) == 4)
			    {
				writer.println();
				writer.println(String.format("%50s", "MAY"));
				month = 4;
			    }
			    if (year.get(Calendar.MONTH) == 5)
			    {
				writer.println();
				writer.println(String.format("%50s", "JUNE"));
				month = 5;
			    }
			    if (year.get(Calendar.MONTH) == 6)
			    {
				writer.println();
				writer.println(String.format("%50s", "JULY"));
				month = 6;
			    }
			    if (year.get(Calendar.MONTH) == 7)
			    {
				writer.println();
				writer.println(String.format("%50s", "AUGUST"));
				month = 7;
			    }
			    if (year.get(Calendar.MONTH) == 8)
			    {
				writer.println();
				writer.println(String.format("%50s", "SEPTEMBER"));
				month = 8;
			    }
			    if (year.get(Calendar.MONTH) == 9)
			    {
				writer.println();
				writer.println(String.format("%50s", "OCTOBER"));
				month = 9;
			    }
			    if (year.get(Calendar.MONTH) == 10)
			    {
				writer.println();
				writer.println(String.format("%50s", "NOVEMBER"));
				month = 10;
			    }
			    if (year.get(Calendar.MONTH) == 11)
			    {
				writer.println();
				writer.println(String.format("%50s", "DECEMBER"));
				month = 11;
			    }
			}
			writer.print(String.format("%10s", x.getStoreID())); //pad with spaces
			writer.print(String.format("%30s", x.getQuantity()));
			writer.print(String.format("%30s", x.getTotalPrice()));
			writer.println();
			    
			yearlyTotal = yearlyTotal + x.getTotalPrice();
			total = total + x.getTotalPrice();
		    }
		}
		writer.println();
		writer.println("TOTAL FOR YEAR: " + yearlyTotal);
		writer.println();
		yearlyTotal = 0;
	    }
	    
	    writer.println("TOTAL FOR ALL SALES: " + total);
	    writer.println();
	    writer.println();
	    writer.flush();
	} 
	catch(SQLException | ClassNotFoundException e)
	{
	    error.writeToLog("DATABASE ERROR");
	}
    }
}