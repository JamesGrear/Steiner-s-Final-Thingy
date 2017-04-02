/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drug.pharmacy.team.pkg110;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author owner
 */
public class DrugPharmacyTeam110 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException 
    {
	int id;
	String password;
	Scanner input = new Scanner(System.in);
        Employee employee = new Employee();
	
	id = input.nextInt();
	password = input.next();
	
	employee.login(id, password);
	
	if(employee.checkValidation())
	{
	    System.out.println(employee.getID() + " " + employee.getManager() + " " + employee.getName());
	    
	}
	else
	{
	    System.out.println("Failed login");
	    System.out.println(employee.getName());
	}
		
    }
    
   
}
