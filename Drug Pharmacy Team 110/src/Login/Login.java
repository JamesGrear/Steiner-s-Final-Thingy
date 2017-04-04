/*
    Created by Robert on 3/31/2017.

    JavaFX Control class for Login Screen.fxml
*/

package Login;

import Database.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import java.sql.SQLException;
import java.lang.String;
import Menu.Menu;

public class Login
{
    @FXML private TextField userIDBox; // Text box for user to enter User ID

    @FXML private PasswordField passwordBox; // Password Box for user to enter password

    // Public no-args constructor
    public Login()
    {
    }

    @FXML private void handleCancelClick()
    // Clears userIDBox and passwordBox
    {
        userIDBox.setText("");
        passwordBox.setText("");
    }

    @FXML private void handleEnterClick(ActionEvent event) throws ClassNotFoundException, SQLException
    // Gets UserID and password from TextBox and PasswordField
    // and checks them against values stored in DB
    {
        try
        {
            int userID = Integer.parseInt(userIDBox.getText()); // this may throw NumberFormatException, see catch block below

            String password = passwordBox.getText();

            Employee user = new Employee(); // contains information about the user (id. name, privilege level)

            user.login(userID, password);

            if(user.checkValidation()) // User is successfully logged in - Load Main menu screen
            {
                /*
                Window loginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

                Menu.launchMenu(user, loginScreen); // launch the main menu interface, passing the user's information and the current window
                */

                // user is now logged in

                // Test dialog for successful login

                Alert Success = new Alert(Alert.AlertType.CONFIRMATION);
                Success.initStyle(StageStyle.UTILITY);
                Success.setTitle("YOU CAN HAZ ACCESS");
                Success.setHeaderText("You are now logged in");
                Success.setContentText("Good jorb.");

                Success.showAndWait();
            }

            else // User ID and password don't match - Error Message
            {
                Alert noAccountFound = new Alert(Alert.AlertType.WARNING);
                noAccountFound.initStyle(StageStyle.UTILITY);
                noAccountFound.setTitle(null);
                noAccountFound.setHeaderText("Invalid User ID or password");
                noAccountFound.setContentText("The User ID and password you have entered do not match.\n\nDouble check that you have entered both correctly.");

                noAccountFound.showAndWait();
            }
        }

        catch(NumberFormatException nfe) // User ID was improperly formatted - Error Message
        {
            Alert invalidID = new Alert(Alert.AlertType.WARNING);
            invalidID.initStyle(StageStyle.UTILITY);
            invalidID.setTitle(null);
            invalidID.setHeaderText("Invalid User ID");
            invalidID.setContentText("The User ID you have entered is invalid.\n\nDouble check that the ID is composed only of numbers and contains no spaces.");

            invalidID.showAndWait();
        }
    }
}