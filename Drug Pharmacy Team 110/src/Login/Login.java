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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    // Runs if the Enter button was clicked. Gets reference to the login window and passes it to try login
    {
        Window loginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        tryLogin(loginScreen); // try to log in with entered credentials
    }

    @FXML private void handleEnterKeyPressed(KeyEvent event) throws ClassNotFoundException, SQLException
    // Runs if the Enter key on the keyboard was pressed. Gets reference to the login window and passes it to try login
    {
        if(event.getCode() == KeyCode.ENTER) // ignore other key presses aside from Enter key
        {
            Window loginScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

            tryLogin(loginScreen); // try to log in with entered credentials
        }
    }

    private void tryLogin(Window loginScreen) throws ClassNotFoundException, SQLException
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
                Menu.launchMenu(user, loginScreen); // launch the main menu interface, passing the user's information and the current window
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