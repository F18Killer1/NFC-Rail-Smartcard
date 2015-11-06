package edu.n0417634.rail;

import java.sql.*;
import java.time.*;
import java.util.*;

public class DatabaseManager 
{
	//final private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	final private static String URL = "jdbc:mysql://localhost:3306/electronicadvancetickets";
	final private static String USERNAME = "SYSTEM";
	final private static String PASSWORD = "5p5$Dmh_YAcA";
	
	private Connection _connection;
	private Card _card;
	private CardReader _reader;
	
	private Calendar _calendar;
	private String _todaysDate;
	
	DatabaseManager()
	{
		_connection = null;
		_todaysDate = null;
		_card = null;
		_reader = null;
		_calendar = new GregorianCalendar();
	}
	
	public void init()
	{	
		connectToDB();
	}
	
	public void updateDate()
	{
		int day =  _calendar.get(Calendar.DAY_OF_MONTH);
		int month =  _calendar.get(Calendar.MONTH) +1;
		int year =  _calendar.get(Calendar.YEAR);
				
		if(day < 10) 
		{ 
			_todaysDate = year + "-" + month + "-" + "0" + day; 
		}
		else 
		{ 
			_todaysDate = year + "-" + month + "-" + day; 
		}
	}
	
	
	//private Boolean validateTimeOnTicket(String ticketTime, String dateOnTicket, String toStation)
	private Boolean validateTimeOnTicket(Ticket ticket)
	{
		//fix this		
		int hoursNow = _calendar.get(Calendar.HOUR_OF_DAY);
		int minutesNow = _calendar.get(Calendar.MINUTE);
		int timeNow = Integer.parseInt(Integer.toString(hoursNow) + Integer.toString(minutesNow));
		
		int timeOnTicket = 0;
		
		String[] time = ticketTime.split(":");
		
		for (int i=0; i< time.length -1; i++)
		{
			timeOnTicket = Integer.parseInt(timeOnTicket + time[i]);
		}
		
		int timeDiff = timeNow - timeOnTicket;

		if(toStation.equals(_reader.getStationName()))
		{
			//not working as they can use the same ticket to get IN the station after going OUT the same one
			
			/*Statement statement = _connection.createStatement();
			String updateIsUsed = "update ticket set isUsed=1 where "
			return statement.executeQuery(query);*/
			return true;
		}
		
		if(timeDiff > 0)
		{
			System.out.println("*-----------------------------*");
			System.out.println("* TICKET *NOT* VALID!");
			System.out.println("*-----------------------------*");
			System.out.println("* EXPIRED AT " + ticketTime + " ON " + dateOnTicket + " ***");
			System.out.println("*-----------------------------*\n");
		}
		else if(timeDiff < -100)	
		{
			System.out.println("*-----------------------------*");
			System.out.println("* TICKET *NOT* VALID!");
			System.out.println("*-----------------------------*");
			System.out.println("* VALID BETWEEN " + (Integer.parseInt(time[0]) -1) + ":"  + time[1] 
					 + " AND " + ticketTime + " TODAY ONLY");
			System.out.println("*-----------------------------*\n");
		}
		else
		{
			return true;
		}		
		return false;
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
		
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() 
		+ " AND (validFromStation='" + _reader.getStationName() + "' OR validToStation='" +  _reader.getStationName() + "')" 
				+" AND validityDate='" + _todaysDate + "' AND isUsed=0;"); 
				
		if (!results.isBeforeFirst()) 
		{    
			System.out.println("*-----------------------------*");
			System.out.println("* TICKET *NOT* VALID!");
			System.out.println("*-----------------------------*");
			System.out.println("* INFORMATION IN SYSTEM DOES NOT MATCH CARD ");
			System.out.println("* PLEASE SEEK ASSISTANCE ");
			System.out.println("*-----------------------------*");
		}
		else
		{
			while(results.next())
			{
				//new imp
				Ticket tkt = new Ticket (results.getInt("ticketID"), results.getInt("cardID"), results.getInt("serviceID"),
						results.getString("validFromStation"), results.getString("validToStation"), results.getString("ticketType"), results.getString("class"), 
						results.getString("purchaseDateTime").toString(), results.getString("validityDate").toString(),
						results.getString("validityTime").toString(), results.getString("seatReservation"), results.getString("ageGroup"), 
						results.getDouble("price"), results.getBoolean("isUsed"));
				
				/*String timeOnTicket = results.getString("validityTime").toString();
				String dateOnTicket = results.getString("validityDate").toString();
				String endStation = results.getString("validToStation").toString();*/
				
				//Boolean isTimeOfScanValid = validateTimeOnTicket(timeOnTicket, dateOnTicket, endStation);
				Boolean isTimeOfScanValid = validateTimeOnTicket(tkt);
				
				if(isTimeOfScanValid)
				{											
					System.out.println("*-----------------------------*");
					System.out.println("* TICKET VALID!");
					System.out.println("*-----------------------------*");
					System.out.println("* OPENING BARRIERS AT " + _reader.getStationName().toUpperCase());
					System.out.println("*-----------------------------*\n");
				}
			}
		}
	}
	
	private void processCardInConductorMode() throws SQLException 
	{
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() 
		+ " AND serviceID=" + _reader.getServiceID() + " AND validityDate='" + _todaysDate + "';");
		
		String ticketID_str = null;
		int ticketID_int = 0;
		
		if (!results.isBeforeFirst()) 
		{    
			 System.out.println("*** TICKET NOT VALID! ***"); 
		}
		else
		{
			while(results.next())
			{
				ticketID_str = results.getString("serviceID");
				ticketID_int = Integer.parseInt(ticketID_str);
				
				if(ticketID_int == _reader.getServiceID())
				{
					String tktType = results.getString("ageGroup") + " " + results.getString("ticketType");
					
					System.out.println("*-----------------------------*");
					System.out.println("* TICKET VALID!");
					System.out.println("*-----------------------------*");
					System.out.println("TYPE  : " + tktType.toUpperCase());
					System.out.println("CLASS : " + results.getString("class"));
					System.out.println("FROM  : " + results.getString("validFromStation"));
					System.out.println("TO    : " + results.getString("validToStation"));
					System.out.println("SEAT  : " + results.getString("seatReservation"));
					System.out.println("*-----------------------------*\n");
				}
			}
		}
	}
	
	
}
