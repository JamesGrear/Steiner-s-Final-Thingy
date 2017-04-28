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
    private int storeID;
    private int quantity;
    private double totalPrice;
    private Date date; 
    private Item item; //A sale currently only holds one item. Could be redone to hold up to 5
    
    //Post: if the ID doesn't exist, adds a new store into the database and returns true
    //      if the ID already exists, returns false
    public void registerNewSale() throws SQLException
    {
	calcTotalPrice();
	
	Database.statement.executeUpdate
		("INSERT INTO sales(iditem, idstore, quantity, totalprice, date)"
		+ "VALUES('" + item.getID() + "','" + Store.getCurrentStoreID() + "','" + quantity + "','" + totalPrice + "','" + date + "')");
    }
    //Post: returns an ArrayList with all of the sales for the store with storeID
    //NOTE: might need to rewrite this to take different parameters for dates
    public static ArrayList<Sales> readAllSales(int itemID) throws SQLException, ClassNotFoundException
    {
	ArrayList<Sales> sales = new ArrayList<>();
	Item item;
	Sales sale;
	

	Database.result2 = Database.statement2.executeQuery("SELECT idstore, quantity, totalprice, date"
							+ " FROM sales WHERE (iditem = '" + itemID + "')");
	
	while(Database.result2.next())
	{
	    sale = new Sales(); 
	    
	    sale.storeID = Database.result2.getInt(1);
	    sale.quantity = Database.result2.getInt(2);
	    sale.totalPrice = Database.result2.getDouble(3);
	    sale.date = Database.result2.getDate(4);
	    
	    System.out.println(sale.storeID);
	    System.out.println(sale.date);
	    
	    item = new Item(itemID);
	    sale.item = item; 
	    sales.add(sale);
	}
	
	return sales;
    }
    public static ArrayList<Sales> readAllSalesForStore(int storeID) throws SQLException, ClassNotFoundException
    {
	ArrayList<Sales> sales = new ArrayList<>();
	Item item;
	Sales sale;
	

	Database.result2 = Database.statement2.executeQuery("SELECT iditem, quantity, totalprice, date"
							+ " FROM sales WHERE (idstore = '" + storeID + "')");
	
	while(Database.result2.next())
	{
	    sale = new Sales(); 
	    
	    item = new Item(Database.result2.getInt(1));
	    sale.quantity = Database.result2.getInt(2);
	    sale.totalPrice = Database.result2.getDouble(3);
	    sale.date = Database.result2.getDate(4);
	    
	    sale.item = item;
	    sale.storeID = storeID;

	    sales.add(sale);
	}
	
	return sales;
    }
    private void calcTotalPrice()
    {
	totalPrice = (item.getCost() * quantity);
    }
    public int getQuantity()
    {
	return quantity;
    }
    public Item getItem()
    {
	return this.item;
    }
    public double getTotalPrice()
    {
	return totalPrice;
    }
    public Date getDate()
    {
	return date;
    }
    public int getStoreID()
    {
	return storeID;
    }
    public void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }
    public void setDate(Date date)
    {
	this.date = date;
    }
    public void setItem(Item item)
    {
	this.item = item;
    }
}