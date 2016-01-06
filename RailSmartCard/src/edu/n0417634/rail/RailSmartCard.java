package edu.n0417634.rail;

import java.sql.SQLException;

public class RailSmartCard 
{
	public static void main(String[] args) throws SQLException 
	{
		CardReaderManager crm = new CardReaderManager();
		Boolean init_success = crm.initialise();
		
		if(init_success)
		{
			crm.start();
		}
		else
		{
			System.out.println("Error loading in stations or services list - machine unoperational!");
			System.exit(1);
		}
	}
}
