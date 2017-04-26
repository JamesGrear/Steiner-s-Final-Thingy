/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transaction;

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
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */
public class CustomerLogin 
{
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
    private void closeWindow(Window customerLoginScreen)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, customerLoginScreen); // launch the main menu interface, passing the user's information and the current window
    }
    @FXML private void handleEnterClick() throws ClassNotFoundException, SQLException
    // Calls function to search for item with matching ID in database
    {
       // tryLookup(); // attempt to find the item in the database and display its attributes
    }
    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window customerLoginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(customerLoginScreen);
    }
}
