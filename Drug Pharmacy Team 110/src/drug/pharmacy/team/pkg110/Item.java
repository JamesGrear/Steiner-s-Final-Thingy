/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drug.pharmacy.team.pkg110;

import java.sql.SQLException;

/**
 *
 * @author Brian
 */
public class Item 
{
    private int id;
    private String name;
    private String warning;
    private double cost;
    
    Item() throws ClassNotFoundException, SQLException
    {
    }
    //Post: adds a new item into the database if the ID didn't already exist. 
    //	    if the ID already exists, returns false
    boolean registerNewItem(int id, String name, int warningLevel, double cost) throws SQLException
    {
	if(!verifyItem(id))
	{
	    Database.statement.executeUpdate("INSERT INTO item(iditem, name, warning, cost)"
			                + "VALUES('" + id + "','" + name + "','" + warningLevel + "','" + cost + "')");
	    return true;
	}
	else
	{
	    return false;
	}
    }
    //Pre : This is a static method. It can only be called from the class itself. Item.readID(id)
    //Post: Returns true if an item with the id exists, else returns false
    static boolean verifyItem(int id) throws SQLException
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
    int getID()
    {
	return id;
    }
    String getName()
    {
	return name;
    }
    String getWarning()
    {
	return warning;
    }
    double getCost()
    {
	return cost;
    }
    void setID(int id)
    {
	this.id = id;
    }
    void setName(String name)
    {
	this.name = name;
    }
    void setWarning(String warning)
    {
	this.warning = warning;
    }
    void setCost(double cost)
    {
	this.cost = cost;
    }
}



