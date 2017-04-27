/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdminMenu;

import Database.Employee;
import Menu.Menu;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.Window;
/**
 *
 * @author Brian
 */
public class SalesReport 
{
    private static Employee User;
    
    @FXML private DatePicker fromDate;    //date boxes
    @FXML private DatePicker toDate;
    
    @FXML private RadioButton dayRB;  //radio buttons for day, week, month, year
    @FXML private RadioButton weekRB;
    @FXML private RadioButton monthRB;
    @FXML private RadioButton yearRB;
    
    @FXML private TextArea textBox; //text area for outputting sales
    
    public static void launchSalesReport(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            Parent root;

            // Set up controller class
            URL location = SalesReport.class.getResource("Sales Report.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            SalesReport contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Sales Report");
            stage.setResizable(false);

            // Runs custom close window function on close request so if user hits red x to close window it takes
            // them back to main menu screen instead of exiting application
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
    @FXML private void handleSalesReport()
    {
	if(toDate.getValue() == null || fromDate.getValue() == null)
	{
	    System.out.println("empty date");
	}
	if(dayRB.isSelected())
	{
	    reportDay();
	}
	else if(weekRB.isSelected())
	{
	    reportWeek();
	}
	else if(monthRB.isSelected())
	{
	    reportMonth();
	}
	else if(yearRB.isSelected())
	{
	    reportYear();
	}
    }
    private void reportDay()
    {
	System.out.println("Day");
    }
    private void reportWeek()
    {
	System.out.println("Week");
    }
    private void reportMonth()
    {
	System.out.println("Month");
    }
    private void reportYear()
    {
	System.out.println("Year");
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
