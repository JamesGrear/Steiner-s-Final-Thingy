/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
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
	Store.setCurrentStoreID(1); //sets current store
	//testEmployee();
	//testCustomer();
	//testItem();
	//testStore();
	testSales();
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
    static void testStore() throws SQLException, ClassNotFoundException
    {
	Store store = new Store(1);
	Store store2;
	boolean registered = false;
	boolean deleted = false;
	boolean updated = false;
	StringBuilder fullAddress = new StringBuilder();
	
	System.out.println("******************STARTING STORE TEST***********************");
	
	store.setAddress("123 Main Street");
	store.setCity("Dearborn");
	store.setState("MI");
	store.setPriority(1);
	store.setZipcode(48128);
	
	registered = store.registerNewStore();
	
	if (registered)
	{
	    fullAddress
		    .append(store.getCity())
		    .append(", ")
		    .append(store.getState())
		    .append(", ")
		    .append(store.getZipcode());
	   
	    System.out.println("Successfully created new Store at location: " + fullAddress);
	}
	else
	{
	    System.out.println("Could not create store. Store already exists");
	}
	
	//deleted = store.deleteStore();
		
	if (deleted == true)
	{
	    System.out.println("Successfully deleted store at location:  " + fullAddress + " with ID #" + store.getID());
	}
	else
	{
	    System.out.println("Failed to delete store. Store does not exist.");
	}
	
	store.setAddress("Updated Address");
	store.setCity("Updated City");
	store.setState("AA");
	store.setZipcode(99999);
	store.setPriority(9);
	
	updated = store.updateStore();
	
	if (updated)
	{
	    fullAddress = new StringBuilder();
	    
	    fullAddress.append(store.getCity())
		    .append(", ")
		    .append(store.getState())
		    .append(", ")
		    .append(store.getZipcode());
	    
	    System.out.println("Successfully updated Store #" + store.getID() + " to address " + fullAddress);
	}
	else
	{
	    System.out.println("Failed to update store. Store does not exist.");
	}
	
	updated = store.updateStoreID(103);
	
	if (updated)
	{
	    System.out.println("Successfully updated store at " + fullAddress + " to #" + store.getID());
	}
	else
	{
	    System.out.println("Failed to update store. Store does not exist or new ID already exists.");
	}
	
	store2 = Store.readStore(104);
	
	if (store2 != null)
	{
	    System.out.println("Store 2:");
	    System.out.println(store2.getID());
	    System.out.println(store2.getAddress());
	    System.out.println(store2.getCity());
	    System.out.println(store2.getState()); 
	    System.out.println(store2.getZipcode());
	    System.out.println(store2.getPriority());
	}
	else
	{
	    System.out.println("Store 2 does not exist in the database");
	}
    }
    
    static void testSales() throws ClassNotFoundException, SQLException
    {
	Sales sale = new Sales();
	ArrayList<Sales> sales;
	    
	System.out.println("******************STARTING STORE TEST***********************");
	
	sale.setItem(Item.readItem(35));  
	sale.setQuantity(5);
	sale.setDate(new Date(117, 3, 7));
	
	sale.registerNewSale();
	
	sales = Sales.readAllSales(1);
	
	for (Sales x : sales)
	{
	    System.out.println("\nITEM: " + x.getItem().getName());
	    System.out.println("QUANTITY: " + x.getQuantity());
	    System.out.println("TOTAL COST: " + x.getTotalPrice());
	    System.out.println("DATE: " + x.getDate());
	}
    }
}