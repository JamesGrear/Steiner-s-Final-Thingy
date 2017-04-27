/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transaction;

import Database.AutoRefills;
import Database.Customer;
import Database.Employee;
import Database.Item;
import Database.Sales;
import Database.Store;
import Database.StoreInventory;
import Menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */
public class Transaction 
{
    private static Employee User; // The user that is currently logged in
    private static Customer customer; //the customer that is currently logged in
    private static Window PrevScreen;
    
    // Text boxes for user input
    @FXML private TextField itemIDBox1;  //holds the 5 itemIDs
    @FXML private TextField itemIDBox2;
    @FXML private TextField itemIDBox3;
    @FXML private TextField itemIDBox4;
    @FXML private TextField itemIDBox5;
    
    @FXML private TextField itemQuantityBox1;  //holds the 5 item quantities
    @FXML private TextField itemQuantityBox2;
    @FXML private TextField itemQuantityBox3;
    @FXML private TextField itemQuantityBox4;
    @FXML private TextField itemQuantityBox5;
    
    @FXML private CheckBox refillCB1;    //holds the 5 check boxes for auto refill
    @FXML private CheckBox refillCB2;
    @FXML private CheckBox refillCB3;
    @FXML private CheckBox refillCB4;
    @FXML private CheckBox refillCB5;
    
    @FXML private TextField refillFrequencyBox1;    //holds the 5 refill frequencies
    @FXML private TextField refillFrequencyBox2;
    @FXML private TextField refillFrequencyBox3;
    @FXML private TextField refillFrequencyBox4;
    @FXML private TextField refillFrequencyBox5;
    
    @FXML private TextField refillBox1;		//holds the 5 boxes for the number of times the item gets refilled
    @FXML private TextField refillBox2;
    @FXML private TextField refillBox3;
    @FXML private TextField refillBox4;
    @FXML private TextField refillBox5;
    
    @FXML private TextField userIDBox;      //secondary user login
    @FXML private TextField userPasswordBox;
    
    
    // Text for program output
    @FXML private Text customerIDBox;	     // Text box to write the customer ID to
    @FXML private Text customerNameBox;	     // Text box to write the customer name to
    @FXML private Text customerAddressBox;	     // Text box to write the customer address to
    @FXML private Text customerRewardBox;	   // Text box to write the customer reward points to

    public Transaction()
    {

    }
    public void launchTransaction(Employee user, Customer customer, Window prevScreen)
    {
        try
        {   
	    User = user;
	    PrevScreen = prevScreen;
	    this.customer = customer;
	    
            Parent root;
	   
            // Set up controller class
            URL location = Transaction.class.getResource("Transactions.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            Transaction contr = fxmlLoader.getController();
	    
            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Transaction");
            stage.setResizable(false);
	    
            stage.setOnCloseRequest(event -> contr.closeWindow(scene.getWindow()));

            stage.setScene(scene);

            stage.show();

            prevScreen.hide(); // Closes the previous screen
	}
	catch(IOException e)
	{
	    e.printStackTrace();
	}
    }
    @FXML private void setCustomerText()
    {
	customerIDBox.setText(Integer.toString(customer.getID()));
	customerNameBox.setText(customer.getName());
	customerAddressBox.setText(customer.getAddress());
	customerRewardBox.setText(Integer.toString(customer.getRewardPoints()));
    }
    @FXML private void tryPurchase(ActionEvent event)
    {
	boolean processItem[] = new boolean[5]; //keeps track of which orders to process in the end
	boolean processRefill[] = new boolean[5]; //keps track of which orders have a refill to process

	Item item[] = new Item[5];
	int quantity[] = new int[5];
	AutoRefills refill[] = new AutoRefills[5];
	StoreInventory storeInventory[] = new StoreInventory[5];
	
	Alert emptyQuantity = new Alert(Alert.AlertType.WARNING);
	emptyQuantity.initStyle(StageStyle.UTILITY);
	emptyQuantity.setTitle(null);
	emptyQuantity.setHeaderText("You have not entered a Quantity.");
	emptyQuantity.setContentText("Please enter a Quantity for each item you purchase.");
	
	Alert emptyRefill = new Alert(Alert.AlertType.WARNING);
	emptyRefill.initStyle(StageStyle.UTILITY);
	emptyRefill.setTitle(null);
	emptyRefill.setHeaderText("You have not entered Auto Refill data.");
	emptyRefill.setContentText("Please enter a Frequency and Refill amount for each auto refill selected.");
	
	Alert invalidItem = new Alert(Alert.AlertType.WARNING);
	invalidItem.initStyle(StageStyle.UTILITY);
	invalidItem.setTitle(null);
	invalidItem.setHeaderText("One or more of the Item IDs you have entered is not in our system.");
	invalidItem.setContentText("Please double check that your Item IDs are correct and try again.");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert badData = new Alert(Alert.AlertType.WARNING);
	badData.initStyle(StageStyle.UTILITY);
	badData.setTitle(null);
	badData.setHeaderText("You have entered invalid data.");
	badData.setContentText("Please check your input and try again.");
	
	Alert noID = new Alert(Alert.AlertType.WARNING);
	noID.initStyle(StageStyle.UTILITY);
	noID.setTitle(null);
	noID.setHeaderText("You are missing one or more Item IDs.");
	noID.setContentText("Please check to make sure you have entered an Item ID for each purchase.");
	
	Alert lowInventory = new Alert(Alert.AlertType.WARNING);
	lowInventory.initStyle(StageStyle.UTILITY);
	lowInventory.setTitle(null);
	lowInventory.setHeaderText("Insufficient stock of item #");
	lowInventory.setContentText("Sorry, we do not have enough stock of item #");
	
	Alert matchingID = new Alert(Alert.AlertType.WARNING);
	matchingID.initStyle(StageStyle.UTILITY);
	matchingID.setTitle(null);
	matchingID.setHeaderText("Matching Item IDs.");
	matchingID.setContentText("You have one or more matching Item IDs. Please keep item of the same type on the same line.");
	
	Alert userError = new Alert(Alert.AlertType.WARNING);
	userError.initStyle(StageStyle.UTILITY);
	userError.setTitle(null);
	userError.setHeaderText("Second Employee must confirm Order.");
	userError.setContentText("A second Employee must enter their User ID and Password to verify the order.");
	
	Alert transactionComplete = new Alert(Alert.AlertType.WARNING);
	transactionComplete.initStyle(StageStyle.UTILITY);
	transactionComplete.setTitle(null);
	transactionComplete.setHeaderText("Your transaction was successful!");
	transactionComplete.setContentText("Thank you for your purchase. Please see cashier to process payment and get your drugs.");
	
	for(int i = 0; i < 5; i++)
	{
	    processItem[i] = false;
	    processRefill[i] = false;
	    refill[i] = new AutoRefills();
	    storeInventory[i] = new StoreInventory();
	    storeInventory[i].setStoreID(Store.getCurrentStoreID());
	}
	
	//if all item ID boxes are empty then don't process 
	if(itemIDBox1.getText().isEmpty() && itemIDBox2.getText().isEmpty() && itemIDBox3.getText().isEmpty()
		&& itemIDBox4.getText().isEmpty() && itemIDBox5.getText().isEmpty())
	{
	    Alert emptyBoxes = new Alert(Alert.AlertType.WARNING);
            emptyBoxes.initStyle(StageStyle.UTILITY);
            emptyBoxes.setTitle(null);
            emptyBoxes.setHeaderText("You have not entered anything.");
            emptyBoxes.setContentText("Please enter at least one Item ID.");

            emptyBoxes.showAndWait();
	    return;
	}
	
	//ATTEMPT TO PROCESS EACH ITEM
	
	//******************************************
	//************ ITEM 1 **********************
	//******************************************
	
	//refill checked or quantity filled but no itemID
	if ((itemIDBox1.getText().isEmpty() && refillCB1.isSelected()) || (itemIDBox1.getText().isEmpty() && !itemQuantityBox1.getText().isEmpty()))
	{
	    noID.showAndWait();
	    return;
	}
	else if(!itemIDBox1.getText().isEmpty())
	{
	    //if quantity is empty then don't process
	    if(itemQuantityBox1.getText().isEmpty())
	    {
		emptyQuantity.showAndWait();
		return;
	    }
	    
	    try
	    {
		item[0] = new Item(Integer.parseInt(itemIDBox1.getText()));
		quantity[0] = Integer.parseInt(itemQuantityBox1.getText());
		
		if(quantity[0] <= 0)
		{
		    emptyQuantity.showAndWait();
		    return;
		}
		
		if(Item.verifyItem(item[0].getID()))
		{
		    //set up the item
		    item[0] = Item.readItem(item[0].getID());
		    storeInventory[0].setItemID(item[0].getID());
		    
		    //check inventory at current store
		    if(storeInventory[0].readInventory() < quantity[0])
		    {
			lowInventory.setHeaderText("Insufficient stock of item ID#" + item[0].getID());
			lowInventory.setContentText("Sorry, we do not have enough stock of item ID#" + item[0].getID() + " to match your order.");
			lowInventory.showAndWait();
			return;
		    }
		    
		    if(refillCB1.isSelected())
		    {
			//if auto refill is selected but info is missing
			if(refillFrequencyBox1.getText().isEmpty() || refillBox1.getText().isEmpty())
			{
			    emptyRefill.showAndWait();
			    return;
			}
			
			//set up the refill
			refill[0].setCustomer(customer);
			refill[0].setAmount(quantity[0]);
			refill[0].setFrequency(Integer.parseInt(refillFrequencyBox1.getText()));
			refill[0].setItem(item[0]);
			refill[0].setDaysUntil(refill[0].getFrequency());
			refill[0].setRemainingRefills(Integer.parseInt(refillBox1.getText()));
			
			if(refill[0].getFrequency() <= 0 || refill[0].getRemainingRefills() <= 0)
			{
			    emptyRefill.showAndWait();
			}
			processRefill[0] = true;
		    }
		    processItem[0] = true;
		}
		else //id is not in system
		{
		    invalidItem.showAndWait();
		    return;
		}
	    }
	    catch(IllegalArgumentException e)
	    {
		badData.showAndWait();
		return;
	    }
	    catch(ClassNotFoundException e)
	    {
		e.printStackTrace();
		return;
	    }
	    catch(SQLException e)
	    {
		dbError.showAndWait();
		return;
	    }
	}
	
	//******************************************
	//************ ITEM 2 **********************
	//******************************************
	
	//refill checked or quantity filled but no itemID
	if ((itemIDBox2.getText().isEmpty() && refillCB2.isSelected()) || (itemIDBox2.getText().isEmpty() && !itemQuantityBox2.getText().isEmpty()))
	{
	    noID.showAndWait();
	    return;
	}
	if(!itemIDBox2.getText().isEmpty())
	{
	    //if quantity is empty then don't process
	    if(itemQuantityBox2.getText().isEmpty())
	    {
		emptyQuantity.showAndWait();
		return;
	    }
	    
	    try
	    {
		item[1] = new Item(Integer.parseInt(itemIDBox2.getText()));
		quantity[1] = Integer.parseInt(itemQuantityBox2.getText());
		
		if(quantity[1] <= 0)
		{
		    emptyQuantity.showAndWait();
		    return;
		}
		
		if(Item.verifyItem(item[1].getID()))
		{
		    //set up the item
		    item[1] = Item.readItem(item[1].getID());
		    
		    storeInventory[1].setItemID(item[1].getID());
		    
		    //check inventory at current store
		    if(storeInventory[1].readInventory() < quantity[1])
		    {
			lowInventory.setHeaderText("Insufficient stock of item ID#" + item[1].getID());
			lowInventory.setContentText("Sorry, we do not have enough stock of item ID#" + item[1].getID() + " to match your order.");
			lowInventory.showAndWait();
			return;
		    }
		    
		    if(refillCB2.isSelected())
		    {
			//if auto refill is selected but info is missing
			if(refillFrequencyBox2.getText().isEmpty() || refillBox2.getText().isEmpty())
			{
			    emptyRefill.showAndWait();
			    return;
			}
			
			//set up the refill
			refill[1].setCustomer(customer);
			refill[1].setAmount(quantity[1]);
			refill[1].setFrequency(Integer.parseInt(refillFrequencyBox2.getText()));
			refill[1].setItem(item[1]);
			refill[1].setDaysUntil(refill[1].getFrequency());
			refill[1].setRemainingRefills(Integer.parseInt(refillBox2.getText()));
			
			if(refill[1].getFrequency() <= 0 || refill[1].getRemainingRefills() <= 0)
			{
			    emptyRefill.showAndWait();
			}
			processRefill[1] = true;
		    }
		    processItem[1] = true;
		}
		else //id is not in system
		{
		    invalidItem.showAndWait();
		    return;
		}
	    }
	    catch(IllegalArgumentException e)
	    {
		badData.showAndWait();
		return;
	    }
	    catch(ClassNotFoundException e)
	    {
		e.printStackTrace();
		return;
	    }
	    catch(SQLException e)
	    {
		dbError.showAndWait();
		return;
	    }
	}
	//******************************************
	//************ ITEM 3 **********************
	//******************************************
	
	//refill checked or quantity filled but no itemID
	if ((itemIDBox3.getText().isEmpty() && refillCB3.isSelected()) || (itemIDBox3.getText().isEmpty() && !itemQuantityBox3.getText().isEmpty()))
	{
	    noID.showAndWait();
	    return;
	}
	if(!itemIDBox3.getText().isEmpty())
	{
	    //if quantity is empty then don't process
	    if(itemQuantityBox3.getText().isEmpty())
	    {
		emptyQuantity.showAndWait();
		return;
	    }
	    
	    try
	    {
		item[2] = new Item(Integer.parseInt(itemIDBox3.getText()));
		quantity[2] = Integer.parseInt(itemQuantityBox3.getText());
		
		if(quantity[2] <= 0)
		{
		    emptyQuantity.showAndWait();
		    return;
		}
		
		if(Item.verifyItem(item[2].getID()))
		{
		    //set up the item
		    item[2] = Item.readItem(item[2].getID());
		    storeInventory[2].setItemID(item[2].getID());
		    
		    //check inventory at current store
		    if(storeInventory[2].readInventory() < quantity[2])
		    {
			lowInventory.setHeaderText("Insufficient stock of item ID#" + item[2].getID());
			lowInventory.setContentText("Sorry, we do not have enough stock of item ID#" + item[2].getID() + " to match your order.");
			lowInventory.showAndWait();
			return;
		    }
		    
		    if(refillCB3.isSelected())
		    {
			//if auto refill is selected but info is missing
			if(refillFrequencyBox3.getText().isEmpty() || refillBox3.getText().isEmpty())
			{
			    emptyRefill.showAndWait();
			    return;
			}
			
			//set up the refill
			refill[2].setCustomer(customer);
			refill[2].setAmount(quantity[2]);
			refill[2].setFrequency(Integer.parseInt(refillFrequencyBox3.getText()));
			refill[2].setItem(item[2]);
			refill[2].setDaysUntil(refill[2].getFrequency());
			refill[2].setRemainingRefills(Integer.parseInt(refillBox3.getText()));
			
			if(refill[2].getFrequency() <= 0 || refill[2].getRemainingRefills() <= 0)
			{
			    emptyRefill.showAndWait();
			}
			processRefill[2] = true;
		    }
		    processItem[2] = true;
		}
		else //id is not in system
		{
		    invalidItem.showAndWait();
		    return;
		}
	    }
	    catch(IllegalArgumentException e)
	    {
		badData.showAndWait();
		return;
	    }
	    catch(ClassNotFoundException e)
	    {
		e.printStackTrace();
		return;
	    }
	    catch(SQLException e)
	    {
		dbError.showAndWait();
		return;
	    }
	}
	//******************************************
	//************ ITEM 4 **********************
	//******************************************
	
	//refill checked or quantity filled but no itemID
	if ((itemIDBox4.getText().isEmpty() && refillCB4.isSelected()) || (itemIDBox4.getText().isEmpty() && !itemQuantityBox4.getText().isEmpty()))
	{
	    noID.showAndWait();
	    return;
	}
	if(!itemIDBox4.getText().isEmpty())
	{
	    //if quantity is empty then don't process
	    if(itemQuantityBox4.getText().isEmpty())
	    {
		emptyQuantity.showAndWait();
		return;
	    }
	    
	    try
	    {
		item[3] = new Item(Integer.parseInt(itemIDBox4.getText()));
		quantity[3] = Integer.parseInt(itemQuantityBox4.getText());
		
		if(quantity[3] <= 0)
		{
		    emptyQuantity.showAndWait();
		    return;
		}
		
		if(Item.verifyItem(item[3].getID()))
		{
		    //set up the item
		    item[3] = Item.readItem(item[3].getID());
		    storeInventory[3].setItemID(item[3].getID());
		    
		    //check inventory at current store
		    if(storeInventory[3].readInventory() < quantity[3])
		    {
			lowInventory.setHeaderText("Insufficient stock of item ID#" + item[3].getID());
			lowInventory.setContentText("Sorry, we do not have enough stock of item ID#" + item[3].getID() + " to match your order.");
			lowInventory.showAndWait();
			return;
		    }
		    
		    if(refillCB4.isSelected())
		    {
			//if auto refill is selected but info is missing
			if(refillFrequencyBox4.getText().isEmpty() || refillBox4.getText().isEmpty())
			{
			    emptyRefill.showAndWait();
			    return;
			}
			
			//set up the refill
			refill[3].setCustomer(customer);
			refill[3].setAmount(quantity[3]);
			refill[3].setFrequency(Integer.parseInt(refillFrequencyBox4.getText()));
			refill[3].setItem(item[3]);
			refill[3].setDaysUntil(refill[3].getFrequency());
			refill[3].setRemainingRefills(Integer.parseInt(refillBox4.getText()));
			
			if(refill[3].getFrequency() <= 0 || refill[3].getRemainingRefills() <= 0)
			{
			    emptyRefill.showAndWait();
			}
			processRefill[3] = true;
		    }
		    processItem[3] = true;
		}
		else //id is not in system
		{
		    invalidItem.showAndWait();
		    return;
		}
	    }
	    catch(IllegalArgumentException e)
	    {
		badData.showAndWait();
		return;
	    }
	    catch(ClassNotFoundException e)
	    {
		e.printStackTrace();
		return;
	    }
	    catch(SQLException e)
	    {
		dbError.showAndWait();
		return;
	    }
	}
	//******************************************
	//************ ITEM 5 **********************
	//******************************************
	
	//refill checked or quantity filled but no itemID
	if ((itemIDBox5.getText().isEmpty() && refillCB5.isSelected()) || (itemIDBox5.getText().isEmpty() && !itemQuantityBox5.getText().isEmpty()))
	{
	    noID.showAndWait();
	    return;
	}
	if(!itemIDBox5.getText().isEmpty())
	{
	    //if quantity is empty then don't process
	    if(itemQuantityBox5.getText().isEmpty())
	    {
		emptyQuantity.showAndWait();
		return;
	    }
	    
	    try
	    {
		item[4] = new Item(Integer.parseInt(itemIDBox5.getText()));
		quantity[4] = Integer.parseInt(itemQuantityBox5.getText());
		
		if(quantity[4] <= 0)
		{
		    emptyQuantity.showAndWait();
		    return;
		}
		
		if(Item.verifyItem(item[4].getID()))
		{
		    //set up the item
		    item[4] = Item.readItem(item[4].getID());
		    storeInventory[4].setItemID(item[4].getID());
		    
		    //check inventory at current store
		    if(storeInventory[4].readInventory() < quantity[4])
		    {
			lowInventory.setHeaderText("Insufficient stock of item ID#" + item[4].getID());
			lowInventory.setContentText("Sorry, we do not have enough stock of item ID#" + item[4].getID() + " to match your order.");
			lowInventory.showAndWait();
			return;
		    }
		    
		    if(refillCB5.isSelected())
		    {
			//if auto refill is selected but info is missing
			if(refillFrequencyBox5.getText().isEmpty() || refillBox5.getText().isEmpty())
			{
			    emptyRefill.showAndWait();
			    return;
			}
			
			//set up the refill
			refill[4].setCustomer(customer);
			refill[4].setAmount(quantity[4]);
			refill[4].setFrequency(Integer.parseInt(refillFrequencyBox5.getText()));
			refill[4].setItem(item[4]);
			refill[4].setDaysUntil(refill[4].getFrequency());
			refill[4].setRemainingRefills(Integer.parseInt(refillBox5.getText()));
			
			if(refill[4].getFrequency() <= 0 || refill[4].getRemainingRefills() <= 0)
			{
			    emptyRefill.showAndWait();
			}
			processRefill[4] = true;
		    }
		    processItem[4] = true;
		}
		else //id is not in system
		{
		    invalidItem.showAndWait();
		    return;
		}
	    }
	    catch(IllegalArgumentException e)
	    {
		badData.showAndWait();
		return;
	    }
	    catch(ClassNotFoundException e)
	    {
		e.printStackTrace();
		return;
	    }
	    catch(SQLException e)
	    {
		dbError.showAndWait();
		return;
	    }
	}
	
	//CHECK IF ANY IDS MATCH. DON'T PROCESS, COULD INTERFERE WITH STOCK
	for(int i = 0; i < 5; i++)
	{
	    //check each id against each id
	   for (int j = 4; j > (i); j--)
	   {
	       if(processItem[i] && processItem[j])
	       {
		   if(item[i].getID() == item[j].getID())
		   {
		       matchingID.showAndWait();
		       return;
		   }
	       }
	   }
	}
	
	//*************************************************
	//********* VERIFIED BY SECOND EMPLOYEE ***********
	//*************************************************
	try
        {
            int userID = Integer.parseInt(userIDBox.getText()); // this may throw NumberFormatException, see catch block below

            String password = userPasswordBox.getText();

            Employee user2 = new Employee(); // contains information about the user (id. name, privilege level)

            user2.login(userID, password);

            if(!user2.checkValidation()) 
            {
                Alert noAccountFound = new Alert(Alert.AlertType.WARNING);
                noAccountFound.initStyle(StageStyle.UTILITY);
                noAccountFound.setTitle(null);
                noAccountFound.setHeaderText("Invalid User ID or password");
                noAccountFound.setContentText("The User ID and password you have entered do not match.\n\nDouble check that you have entered both correctly.");

                noAccountFound.showAndWait();
		return;
            }
	    else
	    {
		if(user2.getID() == User.getID())
		{
		    userError.showAndWait();
		    return;
		}
	    }
        }

        catch(NumberFormatException nfe) // User ID was improperly formatted - Error Message
        {
            Alert invalidID = new Alert(Alert.AlertType.WARNING);
            invalidID.initStyle(StageStyle.UTILITY);
            invalidID.setTitle(null);
            invalidID.setHeaderText("Invalid User ID");
            invalidID.setContentText("The User ID you have entered is invalid.\n\nDouble check that the ID is composed only of numbers and contains no spaces.");

            invalidID.showAndWait();
	    return;
        }
	catch(ClassNotFoundException | SQLException e)
	{
	    dbError.showAndWait();
	    return;
	}
	
	//*************************************************
	//************* PROCESS TRANSACTIONS***************
	//*************************************************
	
	try
	{
	    
	    Sales sale = new Sales();
	    //gets today's date
	    Calendar cal = Calendar.getInstance();
	    int year = cal.get(cal.YEAR);
	    int month = cal.get(cal.MONTH) + 1;
	    int day = cal.get(cal.DATE);
	    
	    java.sql.Date date = java.sql.Date.valueOf(year + "-" + month + "-" + day );
	   
	    for(int i = 0; i < 5; i++)
	    {
		if(processItem[i])
		{
		    //remove inventory from the store
		    storeInventory[i].updateInventory((quantity[i] * -1));
		    
		    //create sales report
		    sale.setDate(date);
		    sale.setItem(item[i]);
		    sale.setQuantity(quantity[i]);
		    
		    //add the sale to the database
		    sale.registerNewSale();
		    
		    customer.addRewardPoints((int)(item[i].getCost() * quantity[i] *.1));
		}
		if(processRefill[i])
		{
		    refill[i].registerNewAutoRefill();
		}
	    }
	}
	catch(SQLException e)
	{
	    e.printStackTrace();
	}
	
	//**************************************************
	//************* RETURN TO MAIN MENU YAY!************
	//**************************************************
	
	transactionComplete.showAndWait();
	
	Window lookupScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(lookupScreen);
    } 
    private void closeWindow(Window lookupScreen)
    // Returns user to main menu and exits the product lookup screen
    {
	Menu.launchMenu(User, lookupScreen); // launch the main menu interface, passing the user's information and the current window
    }
   
    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window lookupScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(lookupScreen);
    }
}