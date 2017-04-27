/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.Database;
import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class Batch 
{
     public static void main(String[] args)
     {
	
	try
	{
	    Database.setupDatabaseConnection();
	}
	catch(ClassNotFoundException | SQLException e)
	{
	    System.out.println("MAJOR DATABASE ERROR");
	    System.out.println("COULD NOT CONNECT TO DATABASE");
	}
	 
	BatchItemUpdate item = new BatchItemUpdate();
	item.readFile();
	
	BatchStoreCreateDelete stores = new BatchStoreCreateDelete();
	stores.readFile();
	
	BatchInventoryToWarehouse inventoryToWarehouse = new BatchInventoryToWarehouse();
	inventoryToWarehouse.readFile();
	
	BatchAutoRefill refills = new BatchAutoRefill();
	refills.refill();
	
	BatchInventoryToStore inventoryToStores = new BatchInventoryToStore();
	inventoryToStores.readFile();
	
	BatchVendorInventoryRequest inventoryRequest = new BatchVendorInventoryRequest();
	inventoryRequest.writeFile();
	
	BatchCalculateSalesReport report = new BatchCalculateSalesReport();
	report.readFile();
     }
}