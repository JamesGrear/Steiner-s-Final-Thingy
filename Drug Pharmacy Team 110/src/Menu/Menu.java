/*
 Created by Robert on 4/4/2017.

 JavaFX Control class for Main Menu.fxml

 This class is responsible for launching the following programs (according to user selection):
    * Transaction Processing
    * Product Lookup
    * Administrative Options Menu
    * Batch Processing
*/

package Menu;

import Database.Employee;
import Main.Main;
import ProductLookup.AdminLookup;
import ProductLookup.Lookup;
import Transaction.CustomerLogin;
import Transaction.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Menu
{
    private static Employee User;

    // Public no-args constructor
    public Menu()
    {
    }

    // Launches the menu screen and closes whatever screen was previously open
    public static void launchMenu(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            // Load main menu

            AnchorPane page = FXMLLoader.load(Menu.class.getResource("Main Menu.fxml"));
            Scene scene = new Scene(page);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Main Menu");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

            prevScreen.hide(); // Closes the previous screen
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void launchPOS(ActionEvent event)
    {
	Window menuScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        CustomerLogin.launchCustomerLogin(User, menuScreen);
    }

    @FXML private void launchProductLookup(ActionEvent event)
    {
        Window menuScreen = ((Node) (event.getSource())).getScene().getWindow(); // get reference to current window

        if(User.getManager())
        {
            AdminLookup.launchProductLookup(User, menuScreen);
        }

        else
        {
            Lookup.launchProductLookup(User, menuScreen);
        }
    }

    @FXML private void launchAdminMenu()
    {
        /*
            TODO: Launch Administration Menu Program from here (pass user and current window)
        */

        if(User.getManager())
        {
            Alert noAccountFound = new Alert(Alert.AlertType.WARNING);
            noAccountFound.initStyle(StageStyle.UTILITY);
            noAccountFound.setTitle(null);
            noAccountFound.setHeaderText("ACCESS GRANTED");
            noAccountFound.setContentText("Wow, you're a manager? That's awesome man, good for you! Come on in and do some admin stuff!");

            noAccountFound.showAndWait();
        }

        else
        {
            Alert noAccountFound = new Alert(Alert.AlertType.WARNING);
            noAccountFound.initStyle(StageStyle.UTILITY);
            noAccountFound.setTitle(null);
            noAccountFound.setHeaderText("ACCESS DENIED: MANAGERS ONLY");
            noAccountFound.setContentText("You lack the required privilege level to access this super exclusive menu.");

            noAccountFound.showAndWait();
        }
    }

    @FXML private void launchBatchProcessing()
    {
	    Batch.Batch.main(null); //start up batch processing
    }

    @FXML private void logout(ActionEvent event)
    {
        Window menuScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        try
        {
            menuScreen.hide();

            User = null; // might be unnecessary, but just in case

            // Launch login screen
            Scene scene = Main.launchLoginScreen();
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Login Screen");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
