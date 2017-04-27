/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdminMenu;

import Database.Employee;
import Database.FileSequence;
import Database.Item;
import Database.Store;
import Menu.Menu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Brian
 */
public class ItemRequest 
{
    final private File fileName = new File("onlinestorerequest.txt");
    private PrintWriter writer;
    private BufferedReader reader;
    private Date date;
    private DateFormat format;
    private static Employee User;
     
    @FXML TextField itemIDBox;
    @FXML TextField itemAmountBox;
     
     // Public no-args constructor
    public ItemRequest()
    {
	try
	{
	    date = new Date();
	    format = new SimpleDateFormat("yyyy-MM-dd");
	    
	    if (!fileName.exists())
	    {
		writer = new PrintWriter(fileName);
		fileName.createNewFile();
		writer.println("HD " + String.format("%04d", FileSequence.readInventoryToStore()) + "      " + format.format(date));
		writer.flush();
		reader = new BufferedReader(new FileReader(fileName));
	    }
	    else
	    {
		reader = new BufferedReader(new FileReader(fileName));
		writer = new PrintWriter(new FileWriter(fileName, true));
	    }
	}
	catch(IOException | SQLException e)
	{
	    e.printStackTrace();
	}
    }

    public static void launchItemRequest(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            Parent root;

            // Set up controller class
            URL location = ItemRequest.class.getResource("Item Request.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            ItemRequest contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Item Request");
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
	
    @FXML private void sendRequest()
    {
	long amount;
	int itemID;
	
	Alert emptyField = new Alert(Alert.AlertType.WARNING);
        emptyField.initStyle(StageStyle.UTILITY);
        emptyField.setTitle(null);
        emptyField.setHeaderText("Empty Field.");
	emptyField.setContentText("Please make sure to fill in every Text Field.");
	    
	Alert badData = new Alert(Alert.AlertType.WARNING);
	badData.initStyle(StageStyle.UTILITY);
	badData.setTitle(null);
	badData.setHeaderText("You have entered invalid data.");
	badData.setContentText("Please check your input and try again.");
	
	Alert invalidID = new Alert(Alert.AlertType.WARNING);
	invalidID.initStyle(StageStyle.UTILITY);
	invalidID.setTitle(null);
	invalidID.setHeaderText("Invalid Item ID.");
	invalidID.setContentText("The Item ID you have entered is not in our system. Please check your input and try again.");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert success = new Alert(Alert.AlertType.WARNING);
	success.initStyle(StageStyle.UTILITY);
	success.setTitle(null);
	    
	if(itemIDBox.getText().isEmpty() || itemAmountBox.getText().isEmpty())
	{
            emptyField.showAndWait();
	    return;
	}
	
	try
	{
	    itemID = Integer.parseInt(itemIDBox.getText());
	    amount = Long.parseLong(itemAmountBox.getText());
	    
	    if(!Item.verifyItem(itemID))
	    {
		invalidID.showAndWait();
		return;
	    }
	    
	    if(amount <= 0)
	    {
		badData.showAndWait();
		return;
	    }
	    
	    if(writeToFile(itemID, amount))
	    {
		success.setHeaderText("Your Request has been sent.");
		success.setContentText("A request has been sent for " + amount + " of item #" + itemID +".");
		success.showAndWait();
	    }
	}
	catch(IllegalArgumentException e)
	{
	    badData.showAndWait();
	    return;
	}
	catch(SQLException e)
	{
	    dbError.showAndWait();
	    return;
	}
    }
    private boolean writeToFile(int itemID, long amount)
    {
	try
	{
	    String storeID = String.format("%05d", Store.getCurrentStoreID());
	    String priority = String.format("%02d", (Store.readStore(Store.getCurrentStoreID()).getPriority()));
	    String id = String.format("%09d", itemID);
	    String quantity = String.format("%010d", amount);
	    String line = (storeID + priority + id + quantity);
	    writer.println(line);
	    writer.flush();
	}
	catch(ClassNotFoundException | SQLException | IllegalArgumentException e)
	{
	    e.printStackTrace();
	    return false;
	}
	return true;
    }
    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window itemRequest = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(itemRequest);
    }
    private void closeWindow(Window adminMenu)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, adminMenu); // launch the main menu interface, passing the user's information and the current window
    }
}
