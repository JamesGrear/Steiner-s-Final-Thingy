/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AdminMenu;

import Database.Employee;
import Database.Sales;
import Menu.Menu;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
/**
 *
 * @author Brian
 */
public class SalesReport 
{
    private static Employee User;
    
    @FXML private DatePicker startDate;    //date boxes
    @FXML private DatePicker endDate;
    
    @FXML private RadioButton dayRB;  //radio buttons for day, week, month, year
    @FXML private RadioButton weekRB;
    @FXML private RadioButton monthRB;
    @FXML private RadioButton yearRB;
    
    @FXML private RadioButton storeRB; //radio Buttons for store, item
    @FXML private RadioButton itemRB;
    
    @FXML private TextArea textBox; //text area for outputting sales
    @FXML private TextField idBox; //store or item ID
    
    public static void launchSalesReport(Employee user, Window prevScreen)
    {
        try
        {
            User = user;

            Parent root;

            // Set up controller class
            URL location = SalesReport.class.getResource("Sales Report.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            root = fxmlLoader.load(location.openStream());
            SalesReport contr = fxmlLoader.getController();

            // Display screen
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Pharmacy Interface - Sales Report");
            stage.setResizable(false);

            // Runs custom close window function on close request so if user hits red x to close window it takes
            // them back to main menu screen instead of exiting application
            stage.setOnCloseRequest(event -> contr.closeWindow(scene.getWindow()));

            stage.setScene(scene);

            stage.show();

            prevScreen.hide(); // Closes the previous screen
        }
	catch(IOException e)
	{
	    e.printStackTrace();
	}
    }
    @FXML private void handleSalesReport()
    {
	LocalDate start;
	LocalDate end;
	int id;
	
	if(startDate.getValue() == null || endDate.getValue() == null) //ERROR: one or two dates not picked
	{
	    Alert nullDate = new Alert(Alert.AlertType.WARNING);
	    nullDate.initStyle(StageStyle.UTILITY);
	    nullDate.setTitle(null);
	    nullDate.setHeaderText("A date hasn't been entered.");
	    nullDate.setContentText("Please enter in a start and end date before trying to get a Sales Report.");
	    
	    nullDate.showAndWait();
	}
	else
	{
	    start = startDate.getValue();
	    end = endDate.getValue();
	    
	    if(start.compareTo(end) > 0) //ERROR: start date happens after end date
	    {
		Alert falseDate = new Alert(Alert.AlertType.WARNING);
		falseDate.initStyle(StageStyle.UTILITY);
		falseDate.setTitle(null);
		falseDate.setHeaderText("Start Date is later than End Date");
		falseDate.setContentText("Please make sure that your End Date comes after your Start Date");
	    
		falseDate.showAndWait();
	    }
	    else if(idBox.getText().isEmpty())
	    {
		Alert emptyText = new Alert(Alert.AlertType.WARNING);
		emptyText.initStyle(StageStyle.UTILITY);
		emptyText.setTitle(null);
		emptyText.setHeaderText("No Store or Item ID entered.");
		emptyText.setContentText("Please enter a Store or Item ID and select the appropriate button.");
	    
		emptyText.showAndWait();
	    }
	   
	    else
	    {
		try
		{
		    id = Integer.parseInt(idBox.getText());
		}
		catch(IllegalArgumentException e)
		{
		    Alert badData = new Alert(Alert.AlertType.WARNING);
		    badData.initStyle(StageStyle.UTILITY);
		    badData.setTitle(null);
		    badData.setHeaderText("You have entered invalid data.");
		    badData.setContentText("Please check your input and try again.");

		    badData.showAndWait();
		    return;
		}
			
		if(itemRB.isSelected()) //item report
		{
		    if(dayRB.isSelected())
		    {
			reportDayItem(start, end, id);
		    }
		    else if(weekRB.isSelected())
		    {
			reportWeekItem(start, end, id);
		    }
		    else if(monthRB.isSelected())
		    {
			reportMonthItem(start, end, id);
		    }
		    else if(yearRB.isSelected())
		    {
			reportYearItem(start, end, id);
		    }
		}
		else if(storeRB.isSelected()) //store report
		{
		    if(dayRB.isSelected())
		    {
			System.out.println("Calling reportDayStore with id#" + id);
			reportDayStore(start, end, id);
		    }
		    else if(weekRB.isSelected())
		    {
			reportWeekStore(start, end, id);
		    }
		    else if(monthRB.isSelected())
		    {
			reportMonthStore(start, end, id);
		    }
		    else if(yearRB.isSelected())
		    {
			reportYearStore(start, end, id);
		    }
		}
	    }
	}
    }
    //*********************************************************
    //******************REPORTS FOR ITEMS**********************
    //*********************************************************
    private void reportDayItem(LocalDate start, LocalDate end, int id)
    {
	int dailyTotal = 0;
	int total = 0;
	LocalDate currentDay = start;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Item #" + id + ".");
	
	textBox.clear();
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSales(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		System.out.println(sales.size());
		return;
	    }
	    else
	    {
		System.out.println(sales.size());
		System.out.println(sales.size());
	    }
	    
	    textBox.appendText(String.format("%80s", "DAILY SALES REPORT FOR ITEM #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "STORE ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n");
	    
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0))
		{
		    if(!currentDay.isEqual(x.getDate().toLocalDate()))
		    {
			//OUPUT DAILY TOTAL
			textBox.appendText("TOTAL FOR ");
			textBox.appendText(currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear());
			textBox.appendText(" = $" + dailyTotal + "\n\n");
			
			currentDay = x.getDate().toLocalDate();
			dailyTotal = 0;
			
			//START NEXT DAY
			textBox.appendText(String.format("%80s", (currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear())) + "\n");  
		    }
		    
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getStoreID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));
		    
		    textBox.appendText(text + "\n");
		    
		    dailyTotal += x.getTotalPrice();
		    total += x.getTotalPrice();
		    hasSales = true;
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT DAILY TOTAL
		textBox.appendText("TOTAL FOR ");
		textBox.appendText(currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear());
		textBox.appendText(" = $" + dailyTotal + "\n\n");
		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportWeekItem(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double weeklyTotal = 0;
	LocalDate startWeek;
	LocalDate endWeek;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Item #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfWeek() != DayOfWeek.SUNDAY) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	startWeek = start;
	endWeek = start.plusDays(6);
	
	while(end.getDayOfWeek() != DayOfWeek.SATURDAY) //set to end of week picked
	{
	    end = end.plusDays(1);
	}
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSales(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    else
	    {
		System.out.println(sales.size());
	    }
	    
	    textBox.appendText(String.format("%80s", "WEEKLY SALES REPORT FOR ITEM #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "STORE ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear())) + "\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endWeek.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startWeek = startWeek.plusDays(7);
			endWeek = endWeek.plusDays(7);
			
			if(endWeek.isBefore(end) || endWeek.isEqual(end))
			{
			    //OUPUT WEEKLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear());
			    textBox.appendText(" = $" + weeklyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    weeklyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear())) + "\n");
			}
			
		    }
		    
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getStoreID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    weeklyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT WEEKLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				+ " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear());
		textBox.appendText(" = $" + weeklyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportMonthItem(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double monthlyTotal = 0;
	LocalDate startMonth;
	LocalDate endMonth;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Item #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfMonth() != 1) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	startMonth = start;
	endMonth = start;
	
	//sets endMonth to the end of the month
	endMonth = endMonth.plusMonths(1);
	endMonth = endMonth.minusDays(1);
	
	//sets end to the end of its month
	end = end.plusMonths(1);
	end = end.minusDays(1);
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSales(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    
	    textBox.appendText(String.format("%80s", "MONTHLY SALES REPORT FOR ITEM #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "STORE ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear())));
	    textBox.appendText("\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endMonth.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startMonth = startMonth.plusMonths(1);
			endMonth = endMonth.plusMonths(1);
			
			if(endMonth.isBefore(end) || endMonth.isEqual(end))
			{
			    //OUPUT MONTHLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear());
			    textBox.appendText(" = $" + monthlyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    monthlyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear())));
			    textBox.appendText("\n");
			}
		    }
		   
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getStoreID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    monthlyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT MONTHLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear());
		textBox.appendText(" = $" + monthlyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportYearItem(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double yearlyTotal = 0;
	LocalDate startYear;
	LocalDate endYear;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Item #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfYear()!= 1) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	
	startYear = start;

	
	//sets endMonth to the end of the year
	endYear = start.plusYears(1);
	endYear = endYear.minusDays(1);
	
	//sets end to the end of its year
	end = end.plusYears(1);
	end = end.minusDays(1);
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSales(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    
	    textBox.appendText(String.format("%80s", "YEARLY SALES REPORT FOR ITEM #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "STORE ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("YEAR OF " + startYear.getYear())));
	    textBox.appendText("\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endYear.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startYear = startYear.plusYears(1);
			endYear = endYear.plusYears(1);
			
			if(endYear.isBefore(end) || endYear.isEqual(end))
			{
			    //OUPUT MONTHLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("YEAR OF " + startYear.getYear());
			    textBox.appendText(" = $" + yearlyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    yearlyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("MONTH OF " + startYear.getYear())));
			    textBox.appendText("\n");
			}
		    }
		   
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getStoreID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    yearlyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT MONTHLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("MONTH OF "  + startYear.getYear());
		textBox.appendText(" = $" + yearlyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    //**********************************************************
    //******************REPORTS FOR STORES**********************
    //**********************************************************
    private void reportDayStore(LocalDate start, LocalDate end, int id)
    {
	System.out.println("REPORTING DAY STORE");
	int dailyTotal = 0;
	int total = 0;
	LocalDate currentDay = start;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Store #" + id + ".");
	
	textBox.clear();
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSalesForStore(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    else
	    {
		System.out.println(sales.size());
	    }
	    
	    textBox.appendText(String.format("%80s", "DAILY SALES REPORT FOR STORE #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "ITEM ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n");
	    
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0))
		{
		    if(!currentDay.isEqual(x.getDate().toLocalDate()))
		    {
			//OUPUT DAILY TOTAL
			textBox.appendText("TOTAL FOR ");
			textBox.appendText(currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear());
			textBox.appendText(" = $" + dailyTotal + "\n\n");
			
			currentDay = x.getDate().toLocalDate();
			dailyTotal = 0;
			
			//START NEXT DAY
			textBox.appendText(String.format("%80s", (currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear())) + "\n");  
		    }
		    
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getItem().getID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));
		    
		    textBox.appendText(text + "\n");
		    
		    dailyTotal += x.getTotalPrice();
		    total += x.getTotalPrice();
		    hasSales = true;
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT DAILY TOTAL
		textBox.appendText("TOTAL FOR ");
		textBox.appendText(currentDay.getMonth() + "/" + currentDay.getDayOfMonth() + "/" + currentDay.getYear());
		textBox.appendText(" = $" + dailyTotal + "\n\n");
		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportWeekStore(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double weeklyTotal = 0;
	LocalDate startWeek;
	LocalDate endWeek;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Store #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfWeek() != DayOfWeek.SUNDAY) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	startWeek = start;
	endWeek = start.plusDays(6);
	
	while(end.getDayOfWeek() != DayOfWeek.SATURDAY) //set to end of week picked
	{
	    end = end.plusDays(1);
	}
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSalesForStore(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    else
	    {
		System.out.println(sales.size());
	    }
	    
	    textBox.appendText(String.format("%80s", "WEEKLY SALES REPORT FOR STORE #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "ITEM ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear())) + "\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endWeek.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startWeek = startWeek.plusDays(7);
			endWeek = endWeek.plusDays(7);
			
			if(endWeek.isBefore(end) || endWeek.isEqual(end))
			{
			    //OUPUT WEEKLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear());
			    textBox.appendText(" = $" + weeklyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    weeklyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				    + " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear())) + "\n");
			}
			
		    }
		    
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getItem().getID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    weeklyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT WEEKLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("WEEK OF " + startWeek.getMonth() + "/" + startWeek.getDayOfMonth() + "/" + startWeek.getYear() 
				+ " - " + endWeek.getMonth() + "/" + endWeek.getDayOfMonth() + "/" + endWeek.getYear());
		textBox.appendText(" = $" + weeklyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportMonthStore(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double monthlyTotal = 0;
	LocalDate startMonth;
	LocalDate endMonth;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for Store #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfMonth() != 1) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	startMonth = start;
	endMonth = start;
	
	//sets endMonth to the end of the month
	endMonth = endMonth.plusMonths(1);
	endMonth = endMonth.minusDays(1);
	
	//sets end to the end of its month
	end = end.plusMonths(1);
	end = end.minusDays(1);
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSalesForStore(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    
	    textBox.appendText(String.format("%80s", "MONTHLY SALES REPORT FOR STORE #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "ITEM ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear())));
	    textBox.appendText("\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endMonth.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startMonth = startMonth.plusMonths(1);
			endMonth = endMonth.plusMonths(1);
			
			if(endMonth.isBefore(end) || endMonth.isEqual(end))
			{
			    //OUPUT MONTHLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear());
			    textBox.appendText(" = $" + monthlyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    monthlyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear())));
			    textBox.appendText("\n");
			}
		    }
		   
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getItem().getID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    monthlyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT MONTHLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("MONTH OF " + startMonth.getMonth() + "/" + startMonth.getYear());
		textBox.appendText(" = $" + monthlyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    private void reportYearStore(LocalDate start, LocalDate end, int id)
    {
	double total = 0;
	double yearlyTotal = 0;
	LocalDate startYear;
	LocalDate endYear;
	String text;
	boolean hasSales = false; //keeps track if sales exist
	DateFormat format = new SimpleDateFormat("MM-dd-yyyy");
	
	Alert dbError = new Alert(Alert.AlertType.WARNING);
	dbError.initStyle(StageStyle.UTILITY);
	dbError.setTitle(null);
	dbError.setHeaderText("Major Database Error.");
	dbError.setContentText("Please check your Database and try again later.");
	
	Alert emptySales = new Alert(Alert.AlertType.WARNING);
	emptySales.initStyle(StageStyle.UTILITY);
	emptySales.setTitle(null);
	emptySales.setHeaderText("No Sales.");
	emptySales.setContentText("There are no Sales to report for STORE #" + id + ".");
	
	textBox.clear();
	
	while(start.getDayOfYear()!= 1) //set to beginning of week picked
	{
	    start = start.minusDays(1);
	}
	
	startYear = start;

	//sets endMonth to the end of the year
	endYear = start.plusYears(1);
	endYear = endYear.minusDays(1);
	
	//sets end to the end of its year
	end = end.plusYears(1);
	end = end.minusDays(1);
	
	try
	{
	    ArrayList<Sales> sales;
	    sales = Sales.readAllSalesForStore(id);
	    
	    if(sales.isEmpty())
	    {
		emptySales.showAndWait();
		return;
	    }
	    
	    textBox.appendText(String.format("%80s", "YEARLY SALES REPORT FOR STORE #") + id + "\n\n");
	    textBox.appendText(String.format("%15s", "DATE"));
	    textBox.appendText(String.format("%20s", "ITEM ID")); //pad with spaces
	    textBox.appendText(String.format("%25s", "QUANTITY SOLD"));
	    textBox.appendText(String.format("%30s", "TOTAL PRICE"));
	    textBox.appendText("\n\n");
	    textBox.appendText(String.format("%80s", ("YEAR OF " + startYear.getYear())));
	    textBox.appendText("\n");
	    //output all sales
	    for(Sales x: sales)
	    {
		if((start.compareTo(x.getDate().toLocalDate()) <= 0) && (end.compareTo(x.getDate().toLocalDate()) >= 0)) //sale exists within timeframe
		{	
		    while((endYear.isBefore(x.getDate().toLocalDate()))) //sale doesnt exist during current week
		    {
			startYear = startYear.plusYears(1);
			endYear = endYear.plusYears(1);
			
			if(endYear.isBefore(end) || endYear.isEqual(end))
			{
			    //OUPUT MONTHLY TOTAL
			    textBox.appendText("TOTAL FOR ");
			    textBox.appendText("YEAR OF " + startYear.getYear());
			    textBox.appendText(" = $" + yearlyTotal + "\n\n");
			
			    //RESET WEEKLY TOTAL
			    yearlyTotal = 0;
			    
			    //START NEXT WEEK
			    textBox.appendText(String.format("%80s", ("MONTH OF " + startYear.getYear())));
			    textBox.appendText("\n");
			}
		    }
		   
		    text = (String.format("%15s", format.format(x.getDate()))
				+ String.format("%15s", x.getItem().getID())
				+ String.format("%30s", x.getQuantity())
				+ String.format("%45s", ("$" + x.getTotalPrice())));

		    textBox.appendText(text + "\n");
		    hasSales = true;
		    
		    total += x.getTotalPrice();
		    yearlyTotal += x.getTotalPrice();
		}
	    }
	    if(hasSales == false)
	    {
		textBox.clear();
		emptySales.showAndWait();
	    }
	    else
	    {
		//OUPUT MONTHLY TOTAL ONE LAST TIME
		textBox.appendText("TOTAL FOR ");
		textBox.appendText("MONTH OF "  + startYear.getYear());
		textBox.appendText(" = $" + yearlyTotal + "\n\n");

		//OUPUT TOTAL TOTAL
		textBox.appendText("FINAL TOTAL = $" + total);
	    }
	}
	catch(SQLException | ClassNotFoundException e)
	{
	    dbError.showAndWait();
	}
    }
    @FXML private void handleCancelClick(ActionEvent event)
    // Call closeWindow function on lookup screen if cancel button is clicked or ESC key is pressed
    {
        Window itemRequest = ((Node)(event.getSource())).getScene().getWindow(); // get reference to current window

        closeWindow(itemRequest);
    }
    private void closeWindow(Window adminMenu)
    // Returns user to main menu and exits the product lookup screen
    {
        Menu.launchMenu(User, adminMenu); // launch the main menu interface, passing the user's information and the current window
    }
}
