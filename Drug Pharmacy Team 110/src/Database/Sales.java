/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Brian
 */
public class Sales 
{
    private int id;
    private int quantity;
    private double totalPrice;
    private Date date; 
    private Item item; //A sale currently only holds one item. Could be redone to hold up to 5
    
    //Post: if the ID doesn't exist, adds a new store into the database and returns true
    //      if the ID already exists, returns false
    void registerNewSale() throws SQLException
    {
	calcTotalPrice();
	
	Database.statement.executeUpdate
		("INSERT INTO sales(iditem, idstore, quantity, totalprice, date)"
		+ "VALUES('" + item.getID() + "','" + Store.getCurrentStoreID() + "','" + quantity + "','" + totalPrice + "','" + date + "')");
    }
    //Post: returns an ArrayList with all of the sales for the store with storeID
    //NOTE: might need to rewrite this to take different parameters for dates
    static ArrayList<Sales> readAllSales(int storeID) throws SQLException, ClassNotFoundException
    {
	ArrayList<Sales> sales = new ArrayList<>();
	Item item;
	Sales sale;
	

	Database.result2 = Database.statement2.executeQuery("SELECT iditem, quantity, totalprice, date"
							+ " FROM sales WHERE (idstore = '" + storeID + "')");
	
	while(Database.result2.next())
	{
	    sale = new Sales(); 
	    
	    sale.quantity = Database.result2.getInt(2);
	    sale.totalPrice = Database.result2.getDouble(3);
	    sale.date = Database.result2.getDate(4);
	    
	    item = Item.readItem(Database.result2.getInt(1)); //read the item id and create an item object with correct variables
	    sale.item = item; 
	    sales.add(sale);
	}
	
	return sales;
    }
    private void calcTotalPrice()
    {
	totalPrice = (item.getCost() * quantity);
    }
    int getQuantity()
    {
	return quantity;
    }
    Item getItem()
    {
	return this.item;
    }
    double getTotalPrice()
    {
	return totalPrice;
    }
    Date getDate()
    {
	return date;
    }
    void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }
    
    void setDate(Date date)
    {
	this.date = date;
    }
    void setItem(Item item)
    {
	this.item = item;
    }
}


