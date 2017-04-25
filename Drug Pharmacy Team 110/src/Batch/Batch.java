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
		ErrorReport error = ErrorReport.getErrorReport();

		try
		{
			Database.setupDatabaseConnection();
		}

		catch(ClassNotFoundException | SQLException e)
		{
			error.writeHeader("MAJOR DATABASE ERROR");
			error.writeToLog("COULD NOT CONNECT TO DATABASE");
			System.out.println("MAJOR DATABASE ERROR");
		}

		BatchItemUpdate item = new BatchItemUpdate();
		BatchStoreCreateDelete stores = new BatchStoreCreateDelete();
		BatchInventoryToWarehouse inventoryToWarehouse = new BatchInventoryToWarehouse();
		BatchAutoRefill refills = new BatchAutoRefill();
		BatchInventoryToStore inventoryToStores = new BatchInventoryToStore();
		BatchVendorInventoryRequest inventoryRequest = new BatchVendorInventoryRequest();
		BatchCalculateSalesReport report = new BatchCalculateSalesReport();

		item.readFile();
		stores.readFile();
		inventoryToWarehouse.readFile();
		refills.refill();
		inventoryToStores.readFile();
		inventoryRequest.writeFile();
		report.readFile();
     }
}