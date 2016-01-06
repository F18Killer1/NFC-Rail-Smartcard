package edu.n0417634.rail;

import java.sql.*;
import java.time.*;
import java.util.*;

public class DatabaseManager 
{ 
	final private static String URL = "jdbc:mysql://localhost:3306/electronicadvancetickets";
	final private static String USERNAME = "SYSTEM";
	final private static String PASSWORD = "5p5$Dmh_YAcA";
	
	private static Connection _connection;
	private Card _card;
	private CardReader _reader;
	private DateTimeController _dtControl;
	private TicketHandler _ticketHandler;
		
	DatabaseManager()
	{
		_connection = null;
		_card = null;
		_reader = null;
	}
	
	public void init()
	{	
		connectToDB();
	}
	
	private void connectToDB()
	{
		try
		{
			_connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Connection to database\t[ SUCCESS ]\n");
		}
		catch(SQLException sqlEx)
		{
			System.out.println("Connection to database\t[ FAILED  ]");
			System.out.println("Exiting...");
			System.exit(1);
		}
	}
	
	public void closeDBConnection()
	{
		try
		{
			_connection.close();
			System.out.println("-> Terminating DB connection\t\t[ SUCCESS ]");
		}
		catch(SQLException sqlEx)
		{
			sqlEx.printStackTrace();
		}
	}
	
	private ResultSet queryDatabase(String query) throws SQLException 
	{
		Statement statement = _connection.createStatement();
		return statement.executeQuery(query);
	}
	
	public static void updateDatabase(String query) throws SQLException
	{
		Statement statement = _connection.createStatement();
		statement.executeUpdate(query);
	}

	public void performDatabaseOperations(Card card, CardReader reader) throws SQLException  
	{
		_card = card;
		_reader = reader;
			
		switch(_reader.getReaderMode())
		{
			case "STATION":
					processCardInStationMode();
					break;
					
			case "CONDUCTOR":
					processCardInConductorMode();
					break;
			
			default: closeDBConnection();
		}
	}
	
	private void processCardInStationMode() throws SQLException 
	{
		_dtControl = new DateTimeController();
		String dateToday = _dtControl.getTodaysDate();
		
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() 
				+ " AND (validFromStation='" + _reader.getStationName() + "' OR validToStation='" +  _reader.getStationName() + "')" 
				+" AND validityDate='" + dateToday + "' AND isUsed=0;");
				
		if (!results.isBeforeFirst()) 
		{    
			printInfoMismatchMessage();
		}
		else
		{
			new TicketHandler(results, _reader);
		}
		results.close();
	}
	
	private void processCardInConductorMode() throws SQLException 
	{
		_dtControl = new DateTimeController();
		String dateToday = _dtControl.getTodaysDate();
		
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() + " AND validityDate='" + dateToday + "' AND isUsed=0;");
				
		if (!results.isBeforeFirst()) 
		{    
			printInfoMismatchMessage();
		}
		else
		{
			new TicketHandler(results, _reader);
			System.out.println("I am at the end of conductor code");
		}
		results.close();
	}
	
	private void printInfoMismatchMessage()
	{
		System.out.println("*-----------------------------*");
		System.out.println("* TICKET *NOT* VALID!");
		System.out.println("*-----------------------------*");
		System.out.println("* INFORMATION MISMATCH ");
		System.out.println("* PLEASE SEEK ASSISTANCE ");
		System.out.println("*-----------------------------*");
	}
}