/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import CurrentStore.CurrentStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import Database.Database;
import Database.Store;


public class Main extends Application
{
    // @param args the command line arguments

    // * Checks for current store file
    // * Sets up database connection
    // * Launches application
    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
        Database.setupDatabaseConnection();

        CurrentStore.main(null);

        System.out.println(Store.getCurrentStoreID());

        if(Store.getStoreSet() == false && Store.getCurrentStoreID() != -1)
        {
            Application.launch(Main.class, (java.lang.String[]) null);
        }
    }

    public static Scene launchLoginScreen() throws IOException
    {
        AnchorPane page = FXMLLoader.load(Main.class.getResource("/Login/Login Screen.fxml"));
        Scene scene = new Scene(page);

        return scene;
    }

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            Scene scene = launchLoginScreen();

            primaryStage.setTitle("Pharmacy Interface - Login Screen");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}