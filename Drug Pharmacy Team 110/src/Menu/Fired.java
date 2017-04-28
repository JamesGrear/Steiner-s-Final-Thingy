/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menu;

import Database.Customer;
import Database.Employee;
import Transaction.Transaction;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */
public class Fired 
{
    private static Employee User;
    private static Window window;
    
    public static void youAreFired(Employee user, Window prevScreen)
    {
        try
        {   
	    User = user;
	    
            Parent root;
	   
            // Set up controller class
            URL location = Fired.class.getResource("Fired.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            Fired contr = fxmlLoader.getController();
	    
            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - FIRED");
            stage.setResizable(false);
	    
            stage.setScene(scene);

            stage.show();
	    
	    window = scene.getWindow();

            prevScreen.hide(); // Closes the previous screen
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
    }
    @FXML private void TRUMP()
    {
	Alert FIRED = new Alert(Alert.AlertType.WARNING);
	FIRED.initStyle(StageStyle.UTILITY);
	FIRED.setTitle(null);
	FIRED.setHeaderText("DOH!");
	FIRED.setContentText("You couldn't follow simple directions, could you? The Button was clearly labeled, 'DO NOT PRESS'. "
	+ " Now you're fired. You have until the end of this sentence to pack up your stuff and leave.");
	FIRED.showAndWait();
	
	Menu.launchMenu(User, window); // launch the main menu interface, passing the user's information and the current window
    }
}
