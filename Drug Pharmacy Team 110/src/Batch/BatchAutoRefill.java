/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Batch;

import Database.AutoRefills;
import Database.Warehouse;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Brian
 */
public class BatchAutoRefill
{
	private ArrayList<AutoRefills> refills;
	private ErrorReport error;

	BatchAutoRefill()
	{
		error = ErrorReport.getErrorReport();
		error.writeHeader("AUTO REFILLS");

		try
		{
			refills = AutoRefills.readAutoRefills(); //gets all refills that are due
		}
		catch(ClassNotFoundException | SQLException e)
		{

			error.writeToLog("DATABASE ERROR");
		}
	}


    public void refill()
    {
		  int daysUntil;

		  try
		  {
			for(AutoRefills x: refills)
			{
			  x.updateDaysUntil(-1); //decrement days until
			  daysUntil = x.getDaysUntil();

			  if(x.getItem() != null) //item exists
			  {
				if (Warehouse.readInventory(x.getItem().getID()) >= x.getAmount()) //warehouse has enough inventory for refill
				{
				  Warehouse.updateInventory(x.getItem().getID(), (x.getAmount() * -1)); //subtract inventory from warehouse
				  x.updateRefillsRemaining(-1); //remove 1 refill remaining

				  if (x.getRemainingRefills() <= 0) {
					x.updateDaysUntil(-1); //decrement days until
					daysUntil = x.getDaysUntil();

								if (x.getItem() != null) //item exists
								{
									if (Warehouse.readInventory(x.getItem().getID()) >= x.getAmount()) //warehouse has enough inventory for refill
									{
										Warehouse.updateInventory(x.getItem().getID(), (x.getAmount() * -1)); //subtract inventory from warehouse
										x.updateRefillsRemaining(-1); //remove 1 refill remaining

										if (x.getRemainingRefills() <= 0)
										{
											x.deleteAutoRefill();
										}

										else
										{
											x.updateDaysUntil(x.getFrequency()); //add frequency to days until, works even if days until is negative due to inventory shortage
										}
									}

									else
									{
										error.writeToLog("WAREHOUSE LACKS INVENTORY OF ITEM #" + x.getItem().getID() + " FOR REFILL #" + x.getID());
									}
								}

								else
								{
									error.writeToLog("COULD NOT REGISTER REFILL #" + x.getID() + " BECAUSE ITEM DOES NOT EXIST");
								}
							}
						}
					}
				}
			}

			catch(SQLException e)
			{
				// e.printStackTrace();
				error.writeToLog("DATABASE ERROR");
			}
		}
}