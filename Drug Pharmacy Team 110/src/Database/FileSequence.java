/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class FileSequence 
{
    private FileSequence()
    {
	
    }
    //Post: Returns the Sequence Number for Create/Delete Store file.
    //	    If something goes wrong, returns -1
    public static int readStoreCreateDelete() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'create_delete_store')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readInventoryToStore() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'inventory_to_store')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readInventoryToWarehouse() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'inventory_to_warehouse')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readInventoryOrder() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'inventory_order')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readItemUpdate() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'item_update')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readYearlySales() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'yearly_sales')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static int readDeletedStoreToWarehouseInventory() throws SQLException
    {
	int num;
	
	Database.result = Database.statement.executeQuery("SELECT seqnum FROM seq WHERE (file = 'deleted_store_to_warehouse_inventory')");
	
	if (Database.result.next())
	{
	    num = Database.result.getInt(1);
	}
	else
	{
	    num = -1;
	}
	
	return num;
    }
    public static boolean incrementStoreCreateDelete() throws SQLException
    {
	int num = readStoreCreateDelete();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'create_delete_store'");
	return true;
    }
    public static boolean incrementInventoryToStore() throws SQLException
    {
	int num = readInventoryToStore();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'inventory_to_store'");
	return true;
    }
    public static boolean incrementInventoryToWarehouse() throws SQLException
    {
	int num = readInventoryToWarehouse();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'inventory_to_warehouse'");
	return true;
    }
    public static boolean incrementInventoryOrder() throws SQLException
    {
	int num = readInventoryOrder();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'inventory_order'");
	return true;
    }
    public static boolean incrementItemUpdate() throws SQLException
    {
	int num = readItemUpdate();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'item_update'");
	return true;
    }
    public static boolean incrementYearlySales() throws SQLException
    {
	int num = readYearlySales();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'yearly_sales'");
	return true;
    }
    public static boolean incrementDeletedStoreToWarehouseInventory() throws SQLException
    {
	int num = readDeletedStoreToWarehouseInventory();
	
	if (num >= 9999)
	{
	    num = 1;
	}
	else if (num <= -1)
	{
	    return false;
	}
	else
	{
	    num++;
	}
	
	Database.statement.executeUpdate("UPDATE seq SET seqnum = '" + num + "' WHERE file = 'deleted_store_to_warehouse_inventory'");
	return true;
    }
}