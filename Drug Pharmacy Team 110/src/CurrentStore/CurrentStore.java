/**
 * Created by Robert on 4/23/2017.
 */

package CurrentStore;

import Database.Store;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CurrentStore extends Application
{
    public static void main(String[] args)
    {
        File curStoreFile = new File("./currentStore.txt"); // path to currentStore file

        // check if currentStore.txt exists, is readable, and is a file (not directory)
        if(curStoreFile.exists() && curStoreFile.canRead() && curStoreFile.isFile())
        {
            try
            {
                Scanner scanner = new Scanner(curStoreFile);

                // check if file contains an int, this should be the store ID
                if(scanner.hasNextInt())
                {
                    int currentStoreID = scanner.nextInt(); // the current store ID, as an integer

                    String storeIDAsString = String.valueOf(currentStoreID); // the store ID as a string, to check size of ID

                    // if store int is correct length, set currentStoreID to value of int
                    if(storeIDAsString.length() <= 5)
                        Store.setCurrentStoreID(currentStoreID);

                    else // int is incorrect length to be storeID -- close scanner, overwrite file
                    {
                        scanner.close();

                        attemptOverwrite(curStoreFile);
                    }
                }

                // no ints in file -- overwrite file
                else
                {
                    scanner.close();

                    attemptOverwrite(curStoreFile);
                }
            }

            catch (IOException ex)
            {
                ex.printStackTrace();
            }

        }

        else
        {
            Application.launch(CurrentStore.class, (java.lang.String[])null);
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            AnchorPane page = FXMLLoader.load(CurrentStore.class.getResource("Current Store Prompt.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setTitle("Pharmacy Interface - Program Setup");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private static void attemptOverwrite(File curStoreFile)
    {
        if(curStoreFile.delete())
        {
            Application.launch(CurrentStore.class, (java.lang.String[])null);
        }

        else // old file deletion failed -- overwrite error
            System.out.println("CRITICAL ERROR: currentStore.txt could not be read in and overwriting file failed. Application aborted.");
    }
}
