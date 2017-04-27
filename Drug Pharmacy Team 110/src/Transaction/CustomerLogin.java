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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */

public class CustomerLogin 
{
    // Text boxes for user input
    @FXML private TextField customerNameBox;	// Text box for user to enter customer name
    @FXML private TextField customerAddressBox; // Text box for user to enter customer address
    @FXML private TextField customerIDBox;		// Text box for user to enter customer ID
    
    private static Employee User; // The user that is currently logged in
    
     // Public no-args constructor
    public CustomerLogin()
    {
    }

    public static void launchCustomerLogin(Employee user, Window prevScreen)
    {
        try
        {
            User = user;
            Parent root;

            // Set up controller class
            URL location = CustomerLogin.class.getResource("Customer account lookup.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            CustomerLogin contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Customer Login");
            stage.setResizable(false);

            stage.setOnCloseRequest(event -> contr.closeWindow(scene.getWindow()));

            stage.setScene(scene);

            stage.show();

            prevScreen.hide(); // Closes the previous screen
		}

		catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void tryLoginName(ActionEvent event)
    {
		String name;
		String address;
		int id;
		Customer customer;

		if(customerNameBox.getText().length() == 0 || customerAddressBox.getText().length() == 0)
		{
			Alert emptyBox = new Alert(Alert.AlertType.WARNING);
			emptyBox.initStyle(StageStyle.UTILITY);
			emptyBox.setTitle(null);
			emptyBox.setHeaderText("You have not entered anything.");
			emptyBox.setContentText("Please enter the customer's Name and Address.");

			emptyBox.showAndWait();
		}

		else
		{
			try
			{
				name = customerNameBox.getText();
				address = customerAddressBox.getText();

				customer = new Customer();

				if(!customer.validateCustomer(name, address))
				{
					//setup new customer
					customer.setName(name);
					customer.setAddress(address);
					customer.setRewardPoints(0);
					customer.registerNewCustomer();
					customer.setID(customer.readID(name, address));

					//alert new user of their new user status
					Alert newUser = new Alert(Alert.AlertType.INFORMATION);
					newUser.initStyle(StageStyle.UTILITY);
					newUser.setTitle(null);
					newUser.setHeaderText("Welcome, new user!");
					newUser.setContentText("You can log in with your name/address or ID from now on. Your ID is " + customer.getID());

					newUser.showAndWait();
				}

				else
				{
					//setup registered customer
					customer.setID(customer.readID(name, address));
					id = customer.getID();
					customer.setAddress(customer.readAddress(id));
					customer.setName(customer.readName(id));
					customer.setRewardPoints(customer.readRewardPoints(id));
				}


				Window loginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window
				Transaction transaction = new Transaction();
				transaction.launchTransaction(User, customer, loginScreen);
			}

			//database issue
			catch (SQLException | ClassNotFoundException e)
			{
				Alert dbError = new Alert(Alert.AlertType.WARNING);
				dbError.initStyle(StageStyle.UTILITY);
				dbError.setTitle(null);
				dbError.setHeaderText("Major Database Error.");
				dbError.setContentText("Please check your Database and try again later.");

				dbError.showAndWait();
			}
		}
    }

    @FXML private void tryLoginID(ActionEvent event)
    {
		int id;
		Customer customer;

		//nothing entered
		if(customerIDBox.getText().length() == 0)
		{
			Alert emptyBox = new Alert(Alert.AlertType.WARNING);
			emptyBox.initStyle(StageStyle.UTILITY);
			emptyBox.setTitle(null);
			emptyBox.setHeaderText("You have not entered anything.");
			emptyBox.setContentText("Please enter the customer's ID.");

			emptyBox.showAndWait();
		}

		else
		{
			//try to parse the string
			try
			{
				id = Integer.parseInt(customerIDBox.getText());

				customer = new Customer();

				//successful login with ID
				if(customer.validateCustomer(id))
				{
					customer.setID(id);
					customer.setAddress(customer.readAddress(id));
					customer.setName(customer.readName(id));
					customer.setRewardPoints(customer.readRewardPoints(id));

					Window loginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

					Transaction transaction = new Transaction();
					transaction.launchTransaction(User, customer, loginScreen);
				}

				else
				{
					Alert invalidID = new Alert(Alert.AlertType.WARNING);
					invalidID.initStyle(StageStyle.UTILITY);
					invalidID.setTitle(null);
					invalidID.setHeaderText("Invalid ID.");
					invalidID.setContentText("The ID you've entered does not match our system data.");

					invalidID.showAndWait();
				}
			}

			//failed to parse
			catch(NumberFormatException e)
			{
				Alert badData = new Alert(Alert.AlertType.WARNING);
				badData.initStyle(StageStyle.UTILITY);
				badData.setTitle(null);
				badData.setHeaderText("You have entered invalid data.");
				badData.setContentText("Please check your input and try again.");

				badData.showAndWait();
			}

			//database issue
			catch (SQLException | ClassNotFoundException e)
			{
				Alert dbError = new Alert(Alert.AlertType.WARNING);
				dbError.initStyle(StageStyle.UTILITY);
				dbError.setTitle(null);
				dbError.setHeaderText("Major Database Error.");
				dbError.setContentText("Please check your Database and try again later.");

				dbError.showAndWait();
			}
		}
    }
    private void closeWindow(Window customerLoginScreen)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, customerLoginScreen); // launch the main menu interface, passing the user's information and the current window
    }

    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window customerLoginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(customerLoginScreen);
    }
}
