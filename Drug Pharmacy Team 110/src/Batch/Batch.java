/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.Database;
import Database.FileSequence;
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
		
		
		/*try
		{
		    FileSequence.incrementDeletedStoreToWarehouseInventory();
		    FileSequence.incrementInventoryOrder();
		    FileSequence.incrementInventoryToStore();
		    FileSequence.incrementInventoryToWarehouse();
		    FileSequence.incrementItemUpdate();
		    FileSequence.incrementStoreCreateDelete();
		    FileSequence.incrementYearlySales();
		}
		catch(SQLException e)
		{
		    e.printStackTrace();
		}*/
     }
}