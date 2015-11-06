package edu.n0417634.rail;

import java.sql.SQLException;

public class RailSmartCard 
{
	public static void main(String[] args) throws SQLException 
	{
		CardReaderManager crm = new CardReaderManager();
		crm.start();
	}
}
