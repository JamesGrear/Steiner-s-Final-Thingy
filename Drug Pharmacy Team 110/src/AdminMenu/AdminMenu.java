/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdminMenu;

import Database.Employee;
import Menu.Menu;
import ProductLookup.Lookup;
import java.io.IOException;
import java.net.URL;
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
public class AdminMenu 
{
    private static Employee User;
     // Public no-args constructor
    public AdminMenu()
    {
    }

    public static void launchAdminMenu(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            Parent root;

            // Set up controller class
            URL location = AdminMenu.class.getResource("Admin Menu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            AdminMenu contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Admin Menu");
            stage.setResizable(false);

            // Runs custom close window function on close request so if user hits red x to close window it takes
            // them back to main menu screen instead of exiting application
            stage.setOnCloseRequest(event -> contr.closeWindow(scene.getWindow()));

            stage.setScene(scene);

            stage.show();

            prevScreen.hide(); // Closes the previous screen
        }
	catch (IOException e)
        {
            e.printStackTrace();
        }
	catch(Exception e)
	{
	    System.out.println(e.getCause());
	    e.printStackTrace();
	}
    }
	
    private void closeWindow(Window adminMenu)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, adminMenu); // launch the main menu interface, passing the user's information and the current window
    }
    
    @FXML private void salesReport(ActionEvent event)
    {
	System.out.println("sales");
    }
    @FXML private void itemRequest(ActionEvent event)
    {
	Window adminMenu = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        ItemRequest.launchItemRequest(User,adminMenu);
    }
    @FXML private void mainMenu(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window adminMenu = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(adminMenu);
    }
}
