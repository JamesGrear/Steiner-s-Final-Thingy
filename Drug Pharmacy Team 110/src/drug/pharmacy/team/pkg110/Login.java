/*
    Created by Robert on 3/31/2017.
*/

package drug.pharmacy.team.pkg110;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.StageStyle;
import java.sql.SQLException;
import java.lang.String;

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
            int userID = Integer.parseInt(userIDBox.getText()); // this may throw NumberFormatException, catch block is below

            // The rest of the code in this try block will only run if an exception was not thrown

            String password = passwordBox.getText();

            Employee user = new Employee();

            user.login(userID, password);

            if(user.checkValidation()) // user is logged in
            {
                // Test dialog for successful login

                Alert Success = new Alert(Alert.AlertType.CONFIRMATION);
                Success.initStyle(StageStyle.UTILITY);
                Success.setTitle("YOU CAN HAZ ACCESS");
                Success.setHeaderText("You are now logged in");
                Success.setContentText("Good jorb.");

                Success.showAndWait();

                /*

                    TODO:
                        * Remove logged in dialog once main menu is set up
                        * Call main menu from here, pass the employee object (user) as parameter

                */

            }

            else
            {
                Alert noAccountFound = new Alert(Alert.AlertType.WARNING);
                noAccountFound.initStyle(StageStyle.UTILITY);
                noAccountFound.setTitle("Invalid User ID or password");
                noAccountFound.setHeaderText("Invalid User ID or password");
                noAccountFound.setContentText("The User ID and password you have entered do not match.\n\nDouble check that you have entered both correctly.");

                noAccountFound.showAndWait();
            }
        }

        catch(NumberFormatException nfe)
        {
            Alert invalidID = new Alert(Alert.AlertType.WARNING);
            invalidID.initStyle(StageStyle.UTILITY);
            invalidID.setTitle("Invalid User ID");
            invalidID.setHeaderText(null);
            invalidID.setContentText("The User ID you have entered is invalid.\n\nDouble check that the ID is composed only of numbers and contains no spaces.");

            invalidID.showAndWait();
        }


    }

}
