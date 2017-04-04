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
	
	customer.setName(name);
	customer.setAddress(address);
	
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
	Item item = new Item();
	
	item.setName("Tylenol");
	item.setID(35);
	item.setCost(3);
	item.setWarning(5);
	item.setDeliveryTime("1 week");
	item.setDescription("This is medicine that does stuff, duh!");
	item.setDosage(9001);
	item.setReorderLevel(1);
	item.setReorderQuantity(10000);
	item.setVendor(3);
	
	boolean registered;
	
	System.out.println("******************STARTING ITEM TEST***********************");
	registered = item.registerNewItem(item);
	
	if (registered == true)
	{
	    System.out.println("Successfully created " + item.getName() + " with ID #" + item.getID());
	}
	else
	{
	    System.out.println("Failed to create item. Item already exists.");
	}
    }
}