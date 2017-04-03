/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drug.pharmacy.team.pkg110;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.SQLException;


public class DrugPharmacyTeam110 extends Application
{
    // @param args the command line arguments

    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
        Application.launch(DrugPharmacyTeam110.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            AnchorPane page = (AnchorPane) FXMLLoader.load(DrugPharmacyTeam110.class.getResource("Login Screen.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Screen");
            primaryStage.show();
        }

        catch (Exception ex)
        {
            Logger.getLogger(DrugPharmacyTeam110.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
