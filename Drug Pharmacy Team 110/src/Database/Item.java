/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import static Database.Database.connection;

/**
 *
 * @author Brian
 */
public class Item 
{
    private int id; //can only be changed by changing the id in the database. that way false data doesn't exist in item objects
    private String name;
    private String description;
    private String dosage;
	private int storeStock;
    private int companyStock;
    private int warning;			// warning code for drug
	private String warningMessage;	// warning message for drug
    private double cost;
    private long reorderLevel;
    private long reorderQuantity;
    private String deliveryTime;
    private int vendorCode;

	// Some statements I've prepared

	// Updates
	private PreparedStatement updateName = connection.prepareStatement("UPDATE item SET NAME = ? WHERE iditem = ?");
	private PreparedStatement updateDescription = connection.prepareStatement("UPDATE item SET description = ? WHERE iditem = ?");
	private PreparedStatement updateStock = connection.prepareStatement("UPDATE store_inventory SET itemquantity = ? WHERE idstore = ? AND iditem = ?");
	private PreparedStatement updateCost = connection.prepareStatement("UPDATE store_inventory SET cost = ? WHERE idstore = ? AND iditem = ?");
	private PreparedStatement insertStoreInventory = connection.prepareStatement("INSERT INTO store_inventory VALUES(?, ?, ?, ?, ?, ?, ?)");
	private PreparedStatement deleteStoreInventory = connection.prepareStatement("DELETE FROM store_inventory WHERE iditem = ?");
	private PreparedStatement deleteWarehouseInventory = connection.prepareStatement("DELETE FROM warehouse_inventory WHERE iditem = ?");
	private PreparedStatement deleteItem = connection.prepareStatement("DELETE FROM item WHERE iditem = ?");

	// Queries
	private PreparedStatement selectFromStoreInventory = connection.prepareStatement("SELECT * FROM store_inventory WHERE idstore = ? AND iditem = ?");
	private PreparedStatement getWarningMessage = connection.prepareStatement("SELECT warningmessage FROM warning WHERE warningcode = (SELECT warning FROM ITEM where iditem = ?)");
	private PreparedStatement selectCostFromStore = connection.prepareStatement("SELECT cost FROM store_inventory WHERE idstore = ? AND iditem = ?");
	private PreparedStatement selectAllStoreStock = connection.prepareStatement("SELECT SUM(itemquantity) FROM store_inventory WHERE iditem = ?");
	private PreparedStatement selectWarehoueStock = connection.prepareStatement("SELECT SUM(itemquantity) FROM warehouse_inventory WHERE iditem = ?");

    public Item(int id) throws ClassNotFoundException, SQLException
    {
		this.id = id;
    }
    //Post: if the ID doesn't exist, adds a new item into the database and returns true
    //	    if the ID already exists, returns false
    public boolean registerNewItem() throws SQLException
    {
		if(!verifyItem(id))
		{
			Database.statement.executeUpdate
			("INSERT INTO item(iditem, name, description, dosage, warning, reorderlevel, reorderquantity, deliverytime, vendorcode)"
			+ "VALUES('" + id + "','" + name + "','" + description + "','" + dosage + "','" + warning + "','"
			+ reorderLevel + "','" + reorderQuantity + "','" + deliveryTime + "','" + vendorCode + "')");
				return true;
		}

		else
		{
			return false;
		}
    }
    //Post: if the ID exists, deletes the pre existing item from the database 
    //	    if the ID did not exist, returns false
    public boolean deleteItem() throws SQLException
    {
		if(verifyItem(id))
		{
			deleteItem.setInt(1, id);
			deleteItem.executeUpdate();

			deleteStoreInventory.setInt(1, id);
			deleteStoreInventory.executeUpdate();

			deleteWarehouseInventory.setInt(1, id);
			deleteWarehouseInventory.executeUpdate();

			return true;
		}

		else
		{
			return false;
		}
    }
    //Post: if the item exists, updates all attributes of a pre-existing item (except id)and returns true
    //	    if the item did not exist, returns false
    public boolean updateItem() throws SQLException
    {
		if(verifyItem(id))
		{
			// Update name in item table
			updateName.setString(1, name);
			updateName.setInt(2, id);
			updateName.executeUpdate();

			// Update description in item table
			updateDescription.setString(1, description);
			updateDescription.setInt(2, id);
			updateDescription.executeUpdate();

			Database.statement.executeUpdate("Update item Set dosage = '" + dosage + "' WHERE iditem = '" + id + "'");
			Database.statement.executeUpdate("Update item Set warning = '" + warning + "' WHERE iditem = '" + id + "'");
			Database.statement.executeUpdate("Update item Set reorderlevel = '" + reorderLevel + "' WHERE iditem = '" + id + "'");
			Database.statement.executeUpdate("Update item Set reorderquantity = '" + reorderQuantity + "' WHERE iditem = '" + id + "'");
			Database.statement.executeUpdate("Update item Set deliverytime = '" + deliveryTime + "' WHERE iditem = '" + id + "'");
			Database.statement.executeUpdate("UPDATE item SET vendorcode = '" + vendorCode + "'WHERE iditem = '" + id + "'");

			// check to see if item already exists in store_inventory
			selectFromStoreInventory.setInt(1, Store.getCurrentStoreID());
			selectFromStoreInventory.setInt(2, id);

			Database.result = selectFromStoreInventory.executeQuery();

			if(Database.result.next())
			{
				// update stock in store_inventory table
				updateStock.setInt(1, storeStock);
				updateStock.setInt(2, Store.getCurrentStoreID());
				updateStock.setInt(3, id);
				updateStock.executeUpdate();

				updateCost.setDouble(1, cost);
				updateCost.setInt(2, Store.getCurrentStoreID());
				updateCost.setInt(3, id);

				updateCost.executeUpdate();
			}

			selectAllStoreStock.setInt(1, id);

			Database.result = selectAllStoreStock.executeQuery();

			if (Database.result.next())
			{
				companyStock = Database.result.getInt(1);
			}

			else
				companyStock = 0;

			selectWarehoueStock.setInt(1, id);

			Database.result = selectWarehoueStock.executeQuery();

			if (Database.result.next())
			{
				companyStock += Database.result.getInt(1);
			}

			return true;
		}

		else
		{
			return false;
		}
    }



    //Post: if the id exists and newID doesn't, updates the id of a pre-existing item to newID in the database, updates object id to newID, returns true
    //	    else, returns false
    public boolean updateItemID(int newID) throws SQLException
    {
		if(verifyItem(id))
		{
			if (!verifyItem(newID))
			{
				Database.statement.executeUpdate("UPDATE item SET iditem = '" + newID + "' WHERE iditem = '" + this.id + "'");
				this.id = newID;
				return true;
			}

			else
			{
				return false;
			}
		}

		else
		{
			return false;
		}
    }
    //Pre : This is a public static method. It is meant only for internal data verification
    //Post: Returns true if an item with the id exists, else returns false
    public static boolean verifyItem(int id) throws SQLException
    {
		Database.result = Database.statement.executeQuery("select iditem from item where iditem = '" + id + "'"); //kind of redundent, but checks if the id exists

		if(Database.result.next())
		{
			return true;
		}

		else
		{
			return false;
		}
    }

    public boolean deleteItemStore() throws SQLException
	{
		if(itemExistsInStore())
		{
			if(getStoreStock() > 0)
			{
				Warehouse.updateInventory(id, getStoreStock());
			}

			deleteStoreInventory.setInt(1, id);
			deleteStoreInventory.executeUpdate();

			return true;
		}

		else
			return false;
	}

    public boolean deleteItemCompany() throws SQLException
	{
		if(itemStockExistsInCompany() == false)
		{
			deleteItem.setInt(1, id);
			deleteItem.executeUpdate();

			deleteStoreInventory.setInt(1, id);
			deleteStoreInventory.executeUpdate();

			deleteWarehouseInventory.setInt(1, id);
			deleteWarehouseInventory.executeUpdate();

			return true;
		}

		else
			return false;
	}

    // returns true if item stock exists anywhere in company
    public boolean itemStockExistsInCompany() throws SQLException
	{
		int total;

		selectAllStoreStock.setInt(1, id);

		Database.result = selectAllStoreStock.executeQuery();

		if (Database.result.next())
		{
			total = Database.result.getInt(1);
		}

		else
			total = 0;

		selectWarehoueStock.setInt(1, id);

		Database.result = selectWarehoueStock.executeQuery();

		if (Database.result.next())
		{
			total += Database.result.getInt(1);
		}

		if(total != 0)
			return true;

		else
			return false;

	}

    // returns true if item exists is registered to store already
    public boolean itemExistsInStore() throws SQLException
	{
		// check to see if item already exists in store_inventory
		selectFromStoreInventory.setInt(1, Store.getCurrentStoreID());
		selectFromStoreInventory.setInt(2, id);

		Database.result = selectFromStoreInventory.executeQuery();

		return Database.result.next();
	}

    public boolean assignToStore() throws SQLException
	{
		if(verifyItem(id))
		{
			if(itemExistsInStore() == false)
			{
				// insert stock in store_inventory table
				insertStoreInventory.setInt(1, Store.getCurrentStoreID());
				insertStoreInventory.setInt(2, id);
				insertStoreInventory.setInt(3, storeStock);
				insertStoreInventory.setNull(4, Types.BIGINT);
				insertStoreInventory.setNull(5, Types.BIGINT);
				insertStoreInventory.setNull(6, Types.BIGINT);
				insertStoreInventory.setDouble(7, cost);
				insertStoreInventory.executeUpdate();

				return true;
			}

			else
				return false;

		}

		else
			return false;
	}

    //Pre : Static method, only called at class level. Returns an initialized item. Initializing item previously to calling this isn't necessary
    //	    i.e. Item item = Item.readItem(id);
    //Post: If an item with the passed in id exists in the database, an item object is returned with all fields set from database
    //	    else a null object is returned;
    public static Item readItem(int id) throws ClassNotFoundException, SQLException
    {
		Item item;

		if (verifyItem(id))
		{
			Database.result = Database.statement.executeQuery("SELECT name, description, dosage, warning, reorderlevel, reorderquantity, deliverytime, vendorcode"
								+ " FROM item WHERE (iditem = '" + id + "')");

			if (Database.result.next())
			{
				item = new Item(id);

				item.name = Database.result.getString(1);
				item.description = Database.result.getString(2);
				item.dosage = Database.result.getString(3);
				item.warning = Database.result.getInt(4);
				item.reorderLevel = Database.result.getLong(5);
				item.reorderQuantity = Database.result.getLong(6);
				item.deliveryTime = Database.result.getString(7);
				item.vendorCode = Database.result.getInt(8);

				Database.result = Database.statement.executeQuery("SELECT itemquantity FROM store_inventory WHERE iditem = '" + id + "' " +
						"AND idstore = '" + Store.getCurrentStoreID() + "'");

				if (Database.result.next())
				{
					item.storeStock = Database.result.getInt(1);
				}

				else
					item.storeStock = 0;

				item.selectAllStoreStock.setInt(1, id);

				Database.result = item.selectAllStoreStock.executeQuery();

				if (Database.result.next())
				{
					item.companyStock = Database.result.getInt(1);
				}

				else
					item.companyStock = 0;

				item.selectWarehoueStock.setInt(1, id);

				Database.result = item.selectWarehoueStock.executeQuery();

				if (Database.result.next())
				{
					item.companyStock += Database.result.getInt(1);
				}

				// Get cost for drug
				item.selectCostFromStore.setInt(1, Store.getCurrentStoreID());
				item.selectCostFromStore.setInt(2, id);

				Database.result = item.selectCostFromStore.executeQuery();

				if(Database.result.next())
				{
					item.cost = Database.result.getDouble(1);
				}

				else
					item.cost = 5;

				// Get warning message for drug
				item.getWarningMessage.setInt(1, id);
				Database.result = item.getWarningMessage.executeQuery();

				if(Database.result.next())
				{
					item.warningMessage = Database.result.getString(1);
				}

				else
					item.warningMessage = "";

				return item;
			}


		}

		return null; //if all else fails
    }
    public int getID()
    {
	return id;
    }
    public String getName()
    {
	return name;
    }
    public int getWarning() { return warning; }
    public double getCost()
    {
	return cost;
    }
    public int getVendorCode()
    {
	return vendorCode;
    }
	public int getStoreStock() { return storeStock; }
    public int getCompanyStock() { return companyStock; }
    public long getReorderLevel()
    {
	return reorderLevel;
    }
    public long getReorderQuantity()
    {
	return reorderQuantity;
    }
	public String getWarningMessage() { return warningMessage; }
	public String getDeliveryTime()
    {
	return deliveryTime;
    }
    public String getDescription()
    {
	return description;
    }
    public String getDosage()
    {
	return dosage;
    }

    public void setId(int id) {this.id = id;}
    public void setName(String name)
    {
	this.name = name;
    }
    public void setWarning(int warning) { this.warning = warning; }
    public void setCost(double cost)
    {
	this.cost = cost;
    }
    public void setStoreStock(int storeStock) { this.storeStock = storeStock; }
    public void setDeliveryTime(String time)
    {
	this.deliveryTime = time;
    }
    public void setDescription(String description)
    {
	this.description = description;
    }
    public void setDosage(String dosage)
    {
	this.dosage = dosage;
    }
    public void setReorderLevel(long reorder)
    {
	this.reorderLevel = reorder;
    }
    public void setReorderQuantity(long quantity)
    {
	this.reorderQuantity = quantity;
    }
    public void setVendorCode(int vendor)
    {
	this.vendorCode = vendor;
    }
}