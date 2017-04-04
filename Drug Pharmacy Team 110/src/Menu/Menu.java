package Menu;

import Database.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Created by Robert on 4/4/2017.
 */

public class Menu
{
    // Public no-args constructor
    public Menu()
    {
    }

    public static void launchMenu(Employee user, Window loginScreen)
    {
        try
        {
            // Load main menu

            AnchorPane page = FXMLLoader.load(Menu.class.getResource("Menu.fxml"));
            Scene scene = new Scene(page);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Main Menu");
            stage.setScene(scene);
            stage.show();

            loginScreen.hide(); // Closes the login screen
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML private void launchPOS()
    {
        /*
            TODO: Launch Transaction Program from here (pass user and current window)
        */
    }

    @FXML private void launchItemLookup()
    {
        /*
            TODO: Launch Item Lookup Program from here (pass user and current window)
        */
    }

    @FXML private void launchAdminMenu()
    {
        /*
            TODO: Launch Administration Menu Program from here (pass user and current window)
        */
    }
}
