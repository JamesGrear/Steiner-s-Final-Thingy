/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Brian
 */
public class Tests 
{
    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
	Database.setupDatabaseConnection(); //this NEEDS to be called in main program or the program will fail
	//testEmployee();
	//testCustomer();
	testItem();
    }
    static void testCustomer() throws SQLException, ClassNotFoundException
    {
	int id;
	String name;
	String address;
	Scanner input = new Scanner(System.in);
	Customer customer = new Customer();
	
	System.out.println("******************STARTING CUSTOMER TEST***********************");
	System.out.print("Enter ID: ");
	id = input.nextInt();
	
	customer.login(id);
	
	if(customer.checkValidation())
	{
	    System.out.println(customer.getID() + " " + customer.getName()+ " " + customer.getAddress() + " " + customer.getRewardPoints());
	}
	else
	{
	    System.out.println("Failed login");
	}
	
	System.out.print("Enter Name: ");
	name = input.next();
	System.out.print("Enter Address: ");
	address = input.next();
	
	customer = new Customer();
	
	customer.login(name, address);

	if(customer.checkValidation())
	{
	    System.out.println(customer.getID() + " " + customer.getName()+ " " + customer.getAddress() + " " + customer.getRewardPoints());
	}
	else
	{   
	    customer.registerNewCustomer(customer);
	    System.out.println("Failed login");
	}
    }
    static void testEmployee() throws SQLException, ClassNotFoundException
    {
	int id;
	String password;
	Scanner input = new Scanner(System.in);
        Employee employee = new Employee();
	
	System.out.println("******************STARTING EMPLOYEE TEST***********************");
	System.out.print("Enter your ID: ");
	id = input.nextInt();
	System.out.print("Enter your password: ");
	password = input.next();
	
	employee.login(id, password);
	
	if(employee.checkValidation())
	{
	    System.out.println(employee.getID() + " " + employee.getManager() + " " + employee.getName());
	}
	else
	{
	    System.out.println("Failed login");
	}	
    }
    static void testItem() throws ClassNotFoundException, SQLException
    {
	Item item = new Item(500);
	Item item2;
	
	item.setName("Tylenol");
	item.setCost(3);
	item.setWarning(5);
	item.setDeliveryTime("1 week");
	item.setDescription("This is medicine that does stuff, duh!");
	item.setDosage(9001);
	item.setReorderLevel(1);
	item.setReorderQuantity(10000);
	item.setVendor(3);
	
	boolean registered = false;
	boolean deleted = false;
	boolean updated = false;
	
	System.out.println("******************STARTING ITEM TEST***********************");
	registered = item.registerNewItem();
	
	if (registered == true)
	{
	    System.out.println("Successfully created " + item.getName() + " with ID #" + item.getID());
	}
	else
	{
	    System.out.println("Failed to create item. Item already exists.");
	}
	
	//deleted = item.deleteItem();
		
	if (deleted == true)
	{
	    System.out.println("Successfully deleted " + item.getName() + " with ID #" + item.getID());
	}
	else
	{
	    System.out.println("Failed to delete item. Item does not exist.");
	}
	
	item.setName("Updated Name");
	item.setCost(99);
	item.setWarning(99);
	item.setDeliveryTime("Updated Time");
	item.setDescription("Updated Description");
	item.setDosage(99);
	item.setReorderLevel(99);
	item.setReorderQuantity(99);
	item.setVendor(99);
	updated = item.updateItem();
	
	if (updated)
	{
	    System.out.println("Successfully updated item #" + item.getID() + " to be called " + item.getName());
	}
	else
	{
	    System.out.println("Failed to update item. Item does not exist.");
	}

	updated = item.updateItemID(103);
	
	if (updated)
	{
	    System.out.println("Successfully updated " + item.getName() + " to #" + item.getID());
	}
	else
	{
	    System.out.println("Failed to update item. Item does not exist or new ID already exists.");
	}

	item2 = Item.readItem(1031);
	
	if (item2 != null)
	{
	    System.out.println("Item 2:");
	    System.out.println(item2.getID());
	    System.out.println(item2.getName());
	    System.out.println(item2.getCost());
	    System.out.println(item2.getWarning()); 
	}
	else
	{
	    System.out.println("Item 2 does not exist in the database");
	}
    }
}