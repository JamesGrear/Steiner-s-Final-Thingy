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
import Database.StoreInventory;
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
import java.util.ArrayList;
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
    private boolean ordered = false;
    private int trailerCount;
     
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
		writer = new PrintWriter(new FileWriter(fileName, false));
		writer.println("HD " + String.format("%04d", FileSequence.readInventoryToStore()) + "      " + format.format(date));
		writer.flush();
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
    @FXML private void orderLowInventory()
    {
	ArrayList<StoreInventory> inventories;
	
	Alert buttonPressed = new Alert(Alert.AlertType.WARNING);
        buttonPressed.initStyle(StageStyle.UTILITY);
        buttonPressed.setTitle(null);
        buttonPressed.setHeaderText("Careful!");
	buttonPressed.setContentText("You seemed to have accidentally clicked this button again!" 
		+(" If you keep clicking so much, you might hurt your finger!"));
	
	Alert order = new Alert(Alert.AlertType.WARNING);
        order.initStyle(StageStyle.UTILITY);
        order.setTitle(null);
        order.setHeaderText("Order Successful!");
	order.setContentText("You've sent an order to replinsh all low inventory! Don't believe me? Go check the file, silly!");
	
	if(ordered)
	{
	    buttonPressed.showAndWait();
	    return;
	}
	
	try
	{
	    inventories = StoreInventory.readAllInventory(Store.getCurrentStoreID());
	    
	    for(StoreInventory x: inventories)
	    {
		if(x.getItemQuantity() <= x.getReorderLevel())
		{
		    writeToFile(x.getItemID(), x.getReorderQuantity());
		    trailerCount++;
		}
	    }
	    
	    ordered = true;
	    order.showAndWait();
	    writeTrailer();
	}
	catch(SQLException e)
	{
	    e.printStackTrace();
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
    private void writeTrailer()
    {
	writer.println("T " + String.format("%04d", trailerCount));
	writer.flush();
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
