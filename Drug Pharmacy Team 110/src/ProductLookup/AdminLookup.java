package ProductLookup;

import Database.Employee;
import Database.Item;
import Database.Store;
import Menu.Menu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Robert on 4/24/2017.
 *
   ▒▒▒▒▒▒▒▒█▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀█
 ▒▒▒▒▒▒▒█░▒▒▒▒▒▒▒▓▒▒▓▒▒▒▒▒▒▒░█
 ▒▒▒▒▒▒▒█░▒▒▓▒▒▒▒▒▒▒▒▒▄▄▒▓▒▒░█░▄▄
 ▒▒▄▀▀▄▄█░▒▒▒▒▒▒▓▒▒▒▒█░░▀▄▄▄▄▄▀░░█
 ▒▒█░░░░█░▒▒▒▒▒▒▒▒▒▒▒█░░░░░░░░░░░█
 ▒▒▒▀▀▄▄█░▒▒▒▒▓▒▒▒▓▒█░░░█▒░░░░█▒░░█
 ▒▒▒▒▒▒▒█░▒▓▒▒▒▒▓▒▒▒█░░░░░░░▀░░░░░█
 ▒▒▒▒▒▄▄█░▒▒▒▓▒▒▒▒▒▒▒█░░█▄▄█▄▄█░░█
 ▒▒▒▒█░░░█▄▄▄▄▄▄▄▄▄▄█░█▄▄▄▄▄▄▄▄▄█
 ▒▒▒▒█▄▄█░░█▄▄█░░░░░░█▄▄█░░█▄▄
 */

public class AdminLookup
{
    Item item = null;

    // Text boxes for user input
    @FXML private TextField productSearchBox; // Text box for user to enter product ID

    // Text boxes for program output
    @FXML private TextField productIDBox;           // Text box to write the product ID to
    @FXML private TextField productNameBox;         // Text box to write the product name to
    @FXML private TextField productPriceBox;        // Text box to write the product price to
    @FXML private TextField productStockBox;        // Text box to write the product stock amount to (amt in current store)
    @FXML private TextField productCompanyStockBox; // Text box to write product stock amount to (amt company wide)
    @FXML private TextArea productDescriptionBox;   // Text box to write the product description to
    @FXML private TextField productDoseBox;

    // Buttons for updating product information

    @FXML private Button nameButton;
    @FXML private Button priceButton;
    @FXML private Button storeButton;
    @FXML private Button descriptionButton;
    @FXML private Button doseButton;

    // Check marks to show user the corresponding attribute was updated

    @FXML private Label prodNameCheck;
    @FXML private Label prodPriceCheck;
    @FXML private Label storeStockCheck;
    @FXML private Label prodDescriptionCheck;
    @FXML private Label prodDoseCheck;

    private static Employee User; // The user that is currently logged in

    // Public no-args constructor
    public AdminLookup()
    {
    }

    public static void launchProductLookup(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            Parent root;

            // Set up controller class
            URL location = Lookup.class.getResource("Admin Product Lookup.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            AdminLookup contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Product Lookup and Update");
            stage.setResizable(false);

            // Runs custom close window function on close request so if user hits red x to close window it takes
            // them back to main menu screen instead of exiting application
            stage.setOnCloseRequest(event -> contr.closeWindow(scene.getWindow()));

            stage.setScene(scene);

            stage.show();

            prevScreen.hide(); // Closes the previous screen
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String convertIDToString(int ID)
    // Converts an integer ID to a zero filled, 9 digit, string ID
    {
        String itemIDAsString = String.valueOf(ID); // set string ID to value of integer ID

        StringBuilder sb = new StringBuilder();

        // Add 0s (if necessary) until ID is 9 digits long
        if(itemIDAsString.length() <= 9)
        {
            while(sb.length() + itemIDAsString.length() < 9)
            {
                sb.append('0');
            }

            sb.append(ID);

            itemIDAsString = sb.toString();

            return itemIDAsString;
        }

        else return "";

    }

    private void closeWindow(Window lookupScreen)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, lookupScreen); // launch the main menu interface, passing the user's information and the current window
    }

    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window lookupScreen = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(lookupScreen);
    }

    @FXML private void resetScreen()
    // Clears product search text box and hides all other text boxes and update buttons from view
    {
        productSearchBox.setText("");

        productIDBox.setVisible(false);

        productNameBox.setVisible(false);
        nameButton.setVisible(false);
        prodNameCheck.setText("");

        productPriceBox.setVisible(false);
        priceButton.setVisible(false);
        prodPriceCheck.setText("");

        productStockBox.setVisible(false);
        storeButton.setVisible(false);
        storeStockCheck.setText("");

        productCompanyStockBox.setVisible(false);

        productDescriptionBox.setVisible(false);
        descriptionButton.setVisible(false);
        prodDescriptionCheck.setText("");

        productDoseBox.setVisible(false);
        doseButton.setVisible(false);
        prodDoseCheck.setText("");

        item = null;
    }

    @FXML private void handleEnterClick() throws ClassNotFoundException, SQLException
    // Calls function to search for item with matching ID in database
    {
        tryLookup(); // attempt to find the item in the database and display its attributes
    }

    @FXML private void handleEnterKeyPressed(KeyEvent event) throws ClassNotFoundException, SQLException
    // Calls function to search for item with matching ID in database
    {
        if(event.getCode() == KeyCode.ENTER) // ignore other key presses aside from Enter key
        {
            tryLookup(); // attempt to find the item in the database and display its attributes
        }
    }

    @FXML private void updateName() throws SQLException
    {
        String updatedName = productNameBox.getText();

        if(updatedName.length() > 0 && updatedName.length() <= 45)
        {
            item.setName(updatedName);

            item.updateItem();

            prodNameCheck.setText("✓");
        }

        else
        {
            Alert nameTooLong = new Alert(Alert.AlertType.WARNING);
            nameTooLong.initStyle(StageStyle.UTILITY);
            nameTooLong.setTitle(null);
            nameTooLong.setHeaderText("Invalid name");
            nameTooLong.setContentText("The name you have entered is too long.\n\nCould you try and be a little more concise?");

            nameTooLong.showAndWait();
        }
    }

    @FXML private void updatePrice() throws SQLException
    {
        try
        {
            String input = productPriceBox.getText();

            input = input.replace("$", "");

            Double updatedPrice = Double.parseDouble(input);

            if(updatedPrice >= 0)
            {
                item.setCost(updatedPrice);

                item.updateItem();

                // Display formatted price
                productPriceBox.setText(String.format("$%.2f", item.getCost()));

                prodPriceCheck.setText("✓");
            }

            else if (input.length() > 0)
            {
                Alert invalidPrice = new Alert(Alert.AlertType.WARNING);
                invalidPrice.initStyle(StageStyle.UTILITY);
                invalidPrice.setTitle(null);
                invalidPrice.setHeaderText("Invalid price");
                invalidPrice.setContentText("The price you have entered is too low.\n\nWe're in the business of making money, not giving it away!");

                invalidPrice.showAndWait();
            }
        }

        catch (NumberFormatException ex)
        {
            Alert invalidPrice = new Alert(Alert.AlertType.WARNING);
            invalidPrice.initStyle(StageStyle.UTILITY);
            invalidPrice.setTitle(null);
            invalidPrice.setHeaderText("Invalid price");
            invalidPrice.setContentText("The price you have entered is not formatted properly.\n\nDouble check that the price is actually a number.");

            invalidPrice.showAndWait();
        }


    }

    @FXML private void updateStore() throws SQLException
    {
        try
        {
            int updatedStock = Integer.parseInt(productStockBox.getText());

            if(updatedStock >= 0)
            {
                item.setStoreStock(updatedStock);

                item.updateItem();

                storeStockCheck.setText("✓");

                // Display updated company-wide stock after stock update
                productCompanyStockBox.setText(Integer.toString(item.getCompanyStock()));
            }

            else
            {
                Alert invalidPrice = new Alert(Alert.AlertType.WARNING);
                invalidPrice.initStyle(StageStyle.UTILITY);
                invalidPrice.setTitle(null);
                invalidPrice.setHeaderText("Invalid stock amount");
                invalidPrice.setContentText("The stock amount you have entered defies logic.\n\nWe can't have negative stock, that's impossible!");

                invalidPrice.showAndWait();
            }

        }

        catch (NumberFormatException ex)
        {
            Alert invalidPrice = new Alert(Alert.AlertType.WARNING);
            invalidPrice.initStyle(StageStyle.UTILITY);
            invalidPrice.setTitle(null);
            invalidPrice.setHeaderText("Invalid stock amount");
            invalidPrice.setContentText("The stock amount you have entered is not formatted properly.\n\nDouble check that the amount is actually a number.");

            invalidPrice.showAndWait();
        }
    }

    @FXML private void updateDescription() throws SQLException
    {
        String updatedDescription = productDescriptionBox.getText();

        if(updatedDescription.length() <= 100)
        {
            item.setDescription(updatedDescription);

            item.updateItem();

            prodDescriptionCheck.setText("✓");
        }

        else if(updatedDescription.length() > 0)
        {
            Alert descriptionTooLong = new Alert(Alert.AlertType.WARNING);
            descriptionTooLong.initStyle(StageStyle.UTILITY);
            descriptionTooLong.setTitle(null);
            descriptionTooLong.setHeaderText("Invalid description");
            descriptionTooLong.setContentText("The description you have entered is too long.\n\nCould you try and be a little more concise?");

            descriptionTooLong.showAndWait();
        }
    }

    @FXML private void updateDose() throws SQLException
    {
        String updatedDose = productDoseBox.getText();

        if(updatedDose.length() <= 20)
        {
            item.setDosage(updatedDose);

            item.updateItem();

            prodDoseCheck.setText("✓");
        }

        else if(updatedDose.length() > 0)
        {
            Alert descriptionTooLong = new Alert(Alert.AlertType.WARNING);
            descriptionTooLong.initStyle(StageStyle.UTILITY);
            descriptionTooLong.setTitle(null);
            descriptionTooLong.setHeaderText("Invalid dosage");
            descriptionTooLong.setContentText("The dosage information you have entered is too long.\n\nCould you try and be a little more concise?");

            descriptionTooLong.showAndWait();
        }
    }

    @FXML private void updateAll() throws SQLException
    {
        if(item != null)
        {
            updateName();
            updatePrice();
            updateStore();
            updateDescription();
            updateDose();
        }
    }

    @FXML private void registerItemToStore() throws SQLException
    {
        if(item != null)
        {
            if (item.assignToStore())
            {
                Alert invalidLength = new Alert(Alert.AlertType.INFORMATION);
                invalidLength.initStyle(StageStyle.UTILITY);
                invalidLength.setTitle(null);
                invalidLength.setHeaderText("Item has been registered to store");
                invalidLength.setContentText(String.format("Your Store (Store #%d)\n\nNow carries item %s\nItem ID: %s", Store.getCurrentStoreID(), item.getName(), item.getID()));

                invalidLength.showAndWait();
            }

            else
            {
                Alert invalidLength = new Alert(Alert.AlertType.WARNING);
                invalidLength.initStyle(StageStyle.UTILITY);
                invalidLength.setTitle(null);
                invalidLength.setHeaderText("Item could not be registered to store");
                invalidLength.setContentText("You already carry this item in your store");

                invalidLength.showAndWait();
            }
        }

        else
        {
            Alert invalidLength = new Alert(Alert.AlertType.WARNING);
            invalidLength.initStyle(StageStyle.UTILITY);
            invalidLength.setTitle(null);
            invalidLength.setHeaderText("No item selected");
            invalidLength.setContentText("You have to select an item first!");

            invalidLength.showAndWait();
        }
    }

    @FXML private void deleteItemCompany() throws SQLException
    {
        if(item != null)
        {
            if(item.deleteItemCompany() == false)
            {
                Alert invalidLength = new Alert(Alert.AlertType.WARNING);
                invalidLength.initStyle(StageStyle.UTILITY);
                invalidLength.setTitle(null);
                invalidLength.setHeaderText("Item could not be deleted at company level");
                invalidLength.setContentText("This item is in the warehouse or other stores, it can't be deleted");

                invalidLength.showAndWait();
            }

            else
            {
                Alert invalidLength = new Alert(Alert.AlertType.INFORMATION);
                invalidLength.initStyle(StageStyle.UTILITY);
                invalidLength.setTitle(null);
                invalidLength.setHeaderText("Item has been deleted from company");
                invalidLength.setContentText(String.format("Your company\nno longer carries item %s\nItem ID: %s", item.getName(), item.getID()));

                invalidLength.showAndWait();
            }

        }

        else
        {
            Alert invalidLength = new Alert(Alert.AlertType.WARNING);
            invalidLength.initStyle(StageStyle.UTILITY);
            invalidLength.setTitle(null);
            invalidLength.setHeaderText("No item selected");
            invalidLength.setContentText("You have to select an item first!");

            invalidLength.showAndWait();
        }
    }

    private void tryLookup() throws ClassNotFoundException, SQLException
    // Attempts to find item with matching ID in database -- if user entered something other than a number
    // An error message will be displayed. If the item doesn't exist, an error message will also be displayed.
    {
        try
        {
            // TODO: implement function to limit number of chars in textfield (http://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield)

            int itemID = Integer.parseInt(productSearchBox.getText()); // this may throw NumberFormatException, see catch block below

            String itemIDAsString = convertIDToString(itemID);  // the item ID displayed as a string (zero filled, 9 digits)
            // -- returns empty string if user entered an ID with more than 9 digits

            if(itemIDAsString.equals("")) // Item ID invalid length error
            {
                Alert invalidLength = new Alert(Alert.AlertType.WARNING);
                invalidLength.initStyle(StageStyle.UTILITY);
                invalidLength.setTitle(null);
                invalidLength.setHeaderText("Invalid item ID length");
                invalidLength.setContentText("The item ID you have entered is of invalid length.\n\nDouble check that the ID is 9 digits or less (leading zeroes can be ignored).");

                invalidLength.showAndWait();
            }

            else // Item ID 9 digits or less, try to find item with matching ID in database
            {
                item = Item.readItem(itemID); // Item object -- if null item does not exist

                if (item != null)
                {
                    // Display full product ID (with leading zeroes)

                    productIDBox.setVisible(true);
                    productIDBox.setText(itemIDAsString);

                    // Display product name

                    productNameBox.setVisible(true);
                    productNameBox.setText(item.getName());
                    prodNameCheck.setText("");
                    nameButton.setVisible(true);

                    // Display product cost

                    productPriceBox.setVisible(true);
                    productPriceBox.setText(String.format("$%.2f", item.getCost()));
                    prodPriceCheck.setText("");
                    priceButton.setVisible(true);

                    // Display number in stock (store level)

                    productStockBox.setVisible(true);
                    productStockBox.setText(Integer.toString(item.getStoreStock()));
                    storeStockCheck.setText("");
                    storeButton.setVisible(true);

                    // Display number in stock (company level)

                    productCompanyStockBox.setVisible(true);
                    productCompanyStockBox.setText(Integer.toString(item.getCompanyStock()));

                    // Display description of product

                    productDescriptionBox.setVisible(true);
                    productDescriptionBox.setText(item.getDescription());
                    prodDescriptionCheck.setText("");
                    descriptionButton.setVisible(true);

                    // Display dosage information

                    productDoseBox.setVisible(true);
                    productDoseBox.setText(item.getDosage());
                    prodDoseCheck.setText("");
                    doseButton.setVisible(true);
                }

                else
                {
                    Alert noItemFound = new Alert(Alert.AlertType.CONFIRMATION);
                    noItemFound.initStyle(StageStyle.UTILITY);
                    noItemFound.setTitle(null);
                    noItemFound.setHeaderText("No items found");
                    noItemFound.setContentText(String.format("There is no item with matching ID: %s registered to the company.\n\nDo you wish to create a new item with ID #%s?\n Item will contain default values so you must manually update after registering item.", itemIDAsString, itemIDAsString));

                    Optional<ButtonType> result = noItemFound.showAndWait();

                    if(result.get() == ButtonType.OK)
                    {
                        item = new Item(itemID);

                        item.setName("Default Name");
                        item.setDescription("Default Description");
                        item.setDosage("0 mg");
                        item.setWarning(0);
                        item.setReorderLevel(10000);
                        item.setReorderQuantity(10000);
                        item.setDeliveryTime("One Week");
                        item.setVendorCode(1);

                        item.registerNewItem();
                    }
                }
            }
        }

        catch(NumberFormatException nfe)
        {
            Alert invalidFormat = new Alert(Alert.AlertType.WARNING);
            invalidFormat.initStyle(StageStyle.UTILITY);
            invalidFormat.setTitle(null);
            invalidFormat.setHeaderText("Invalid item ID formatting");
            invalidFormat.setContentText("The item ID you have entered is improperly formatted.\n\nDouble check that the ID is composed only of numbers (no spaces/separators).");

            invalidFormat.showAndWait();
        }
    }
}
