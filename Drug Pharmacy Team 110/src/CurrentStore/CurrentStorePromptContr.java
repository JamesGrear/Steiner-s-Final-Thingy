/**
 * Created by Robert on 4/24/2017.
 */

package CurrentStore;

import Database.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import Main.Main;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class CurrentStorePromptContr implements Initializable
{
    File curStoreFile;

    Window curStorePrompt;

    @FXML TextField storeIDTextBox;

    public CurrentStorePromptContr()
    {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        curStoreFile = new File("./currentStore.txt");

        try
        {
            curStoreFile.createNewFile();
        }

        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @FXML private void handleEnterClick(ActionEvent event)
    {
        curStorePrompt = ((Node)(event.getSource())).getScene().getWindow();

        setStoreID();
    }

    @FXML private void handleEnterKeyPressed(KeyEvent event)
    {
        if(event.getCode() == KeyCode.ENTER)
        {
            curStorePrompt = ((Node)(event.getSource())).getScene().getWindow();

            setStoreID();
        }
    }

    private void setStoreID()
    // This will run when the user hits the enter button or presses the enter key
    // This function sets the currentStoreID in Database.Store to the int value of the user entered string
    // It also calls saveIDToFile so the string can be saved to a file to be read in next time the program runs
    {
        String storeIDAsString = storeIDTextBox.getText();

        if(storeIDAsString.length() <= 5)
        {
            try
            {
                int storeID = Integer.parseInt(storeIDAsString);

                Store.setCurrentStoreID(storeID);

                saveIDToFile(storeIDAsString);
            }

            catch(NumberFormatException nfe)
            {
                Alert invalidID = new Alert(Alert.AlertType.WARNING);
                invalidID.initStyle(StageStyle.UTILITY);
                invalidID.setTitle(null);
                invalidID.setHeaderText("Invalid store ID formatting");
                invalidID.setContentText("The Store ID you have entered is improperly formatted.\n\nDouble check that the ID is composed only of numbers (no spaces/separators).");

                invalidID.showAndWait();
            }
        }

        else
        {
            Alert invalidID = new Alert(Alert.AlertType.WARNING);
            invalidID.initStyle(StageStyle.UTILITY);
            invalidID.setTitle(null);
            invalidID.setHeaderText("Invalid ID length");
            invalidID.setContentText("The Store ID you have entered is of invalid length.\n\nDouble check that the ID is 5 digits or less (leading zeroes can be ignored).");

            invalidID.showAndWait();
        }
    }

    private void saveIDToFile(String ID)
    // This function writes a string to the curStoreFile and sets the file to read only after writing the string
    // Prompt screen will be closed and login screen will be launched from here as well
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(curStoreFile);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            bw.write(ID);

            bw.close();

            curStoreFile.setReadOnly();

            // Hide current screen
            curStorePrompt.hide();

            // Launch login screen
            Scene scene = Main.launchLoginScreen();
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Login Screen");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
