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
    int id;
    int quantity;
    double totalPrice;
    Date date; 
    Item item;
    
    //Post: if the ID doesn't exist, adds a new store into the database and returns true
    //      if the ID already exists, returns false
    void registerNewSale() throws SQLException
    {
	Database.statement.executeUpdate
		("INSERT INTO sales(iditem, idstore, quantity, totalprice, date)"
		+ "VALUES('" + item.getID() + "','" + Store.getCurrentStoreID() + "','" + quantity + "','" + totalPrice + "','" + date + "')");
    }
    static ArrayList<Sales> getAllSales(int storeID) throws SQLException, ClassNotFoundException
    {
	ArrayList<Sales> sales = new ArrayList<>();
	Sales sale;
	
	Database.result = Database.statement.executeQuery("SELECT iditem, quantity, totalprice, date"
							+ " FROM sales WHERE (idstore = '" + storeID + "')");
	
	while(Database.result.next())
	{
	    sale = new Sales();
	    
	    sale.setItem(new Item(Database.result.getInt(1)));  //just gets item id, need to get all attributes item also needs to be verified
	    sale.setQuantity(Database.result.getInt(2));
	    sale.setTotalPrice(Database.result.getDouble(3));
	    sale.setDate(Database.result.getDate(4));
	    
	    sales.add(sale);
	}
	
	return sales;
    }
    Item getItem()
    {
	return this.item;
    }
    void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }
    void setTotalPrice(double total)
    {
	this.totalPrice = total;
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


