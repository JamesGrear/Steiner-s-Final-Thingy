/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.Database;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class Tests 
{
    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException
     {
	 Database.setupDatabaseConnection();
	 testBatchStoreUpdate();
	 //testErrorReport();
     }
    
    static void testBatchStoreUpdate() throws ClassNotFoundException, SQLException, FileNotFoundException
    {
	BatchStoreCreateDelete store = new BatchStoreCreateDelete();
	
	store.ReadFile();
    }
    static void testErrorReport()
    {
	ErrorReport error = new ErrorReport();
	
	error.writeHeader("THIS IS HEADER FILE STUFF");
    }
}