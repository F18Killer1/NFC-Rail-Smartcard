package edu.n0417634.rail;

import java.sql.*;
import java.time.*;

public class DatabaseManager 
{
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	final static String URL = "jdbc:mysql://localhost:3306/electronicadvancetickets";
	final static String USERNAME = "SYSTEM";
	final static String PASSWORD = "5p5$Dmh_YAcA";
	Connection _connection;
	String _todaysDate;
	Card _card;
	CardReader _reader;
	
	DatabaseManager()
	{
		_connection = null;
		_todaysDate = null;
		_card = null;
		_reader = null;
	}
	
	public void init()
	{	
		connectToDB();
	}
	
	public void updateDate()
	{
		LocalDate today = LocalDate.now();
		int day = today.getDayOfMonth();
		int mon = today.getMonthValue();
		int yr = today.getYear();
		
		if(day < 10) 
		{ 
			_todaysDate = yr + "-" + mon + "-" + "0" + day; 
		}
		else 
		{ 
			_todaysDate = yr + "-" + mon + "-" + day; 
		}
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
		//same as conductor mode but the following...
		
		/*
		 * 1. Check that the date on the ticket is correct
		 * 2. check the time is less than or greater than 1hr before ticket time
		 * 3. check station is correct (can pass through or not)
		 * 
		 * */
	}
	
	
	private void processCardInConductorMode() throws SQLException 
	{
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() + " AND serviceID=" + _reader.getServiceID() + " AND validityDate='" + _todaysDate + "';");
		
		String ticketID_str = null;
		int ticketID_int = 0;
		
		if (!results.isBeforeFirst()) 
		{    
			 System.out.println("*** TICKET MOT VALID! ***"); 
		}
		else
		{
			while(results.next())
			{
				ticketID_str = results.getString("serviceID");
				ticketID_int = Integer.parseInt(ticketID_str);
				
				if(ticketID_int == _reader.getServiceID())
				{
					System.out.println("*-----------------------------*");
					System.out.println("* TICKET VALID!");
					System.out.println("*-----------------------------*");
					System.out.println("TYPE  : " + results.getString("ticketType"));
					System.out.println("FROM  : " + results.getString("validFromStation"));
					System.out.println("TO    : " + results.getString("validToStation"));
					System.out.println("CLASS : " + results.getString("class"));
					System.out.println("SEAT  : " + results.getString("seatReservation"));
					System.out.println("*-----------------------------*");
				}
			}
		}
	}
	
	
}
