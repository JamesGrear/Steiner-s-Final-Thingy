/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transaction;

import Database.Customer;
import Database.Employee;
import Menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */
public class Transaction 
{
    private static Employee User; // The user that is currently logged in
    //private static Customer Customer; //the customer that is currently logged in
    private static Customer customer;
    //private static Window PrevScreen;
    
    // Text boxes for user input

    // Text boxes for program output
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
	    //PrevScreen = prevScreen;
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
	catch (LoadException e)
        {
            e.printStackTrace();
	    System.out.println(e.getCause());
        }
	catch(IOException e)
	{
	    
	}
    }
    @FXML private void setCustomerText()
    {
	customerIDBox.setText(Integer.toString(customer.getID()));
	customerNameBox.setText(customer.getName());
	customerAddressBox.setText(customer.getAddress());
	customerRewardBox.setText(Integer.toString(customer.getRewardPoints()));
    }
    
    private void closeWindow(Window lookupScreen)
    // Returns user to main menu and exits the product lookup screen
    {
	Menu.launchMenu(User, lookupScreen); // launch the main menu interface, passing the user's information and the current window
    }
   
    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
       // Window lookupScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        //closeWindow(lookupScreen);
    }
}
