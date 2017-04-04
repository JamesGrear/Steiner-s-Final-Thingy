/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.SQLException;
import Database.Database;


public class Main extends Application
{
    // @param args the command line arguments

    // Sets up database connection and launches application
    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
        Database.setupDatabaseConnection();
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            AnchorPane page = FXMLLoader.load(Main.class.getResource("/Login/Login Screen.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Pharmacy Interface - Login Screen");
            primaryStage.show();
        }

        catch (Exception ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}