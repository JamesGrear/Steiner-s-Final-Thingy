/*
    Created by Robert on 3/31/2017.
*/

package drug.pharmacy.team.pkg110;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javafx.scene.control.PasswordField;

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

    @FXML private void handleEnterClick(ActionEvent event)
    // Gets UserID and password from TextBox and PasswordField
    // and checks them against values stored in DB
    {
        String userID = userIDBox.getText();

        String password = passwordBox.getText();

        // System.err.println("IT WORKED"); // Test to see if button works

        /*
        TODO:
                * Get database schema
                * Insert test values into db
                * Update handleEnterClick function
                * Try to login

        */
    }

}
