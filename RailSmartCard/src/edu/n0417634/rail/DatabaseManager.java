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
	private DateTimeController _dtControl;
		
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
	
	private void updateDatabase(String query) throws SQLException
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
					new TicketHandler(_reader.getReaderMode());
					break;
					
			case "CONDUCTOR":
					processCardInConductorMode();
					new TicketHandler(_reader.getReaderMode());
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
			while(results.next())
			{
				Ticket tkt = new Ticket (results.getInt("ticketID"), results.getInt("cardID"), results.getInt("serviceID"),
						results.getString("validFromStation"), results.getString("validToStation"), results.getString("ticketType"), results.getString("class"), 
						results.getString("purchaseDateTime").toString(), results.getString("validityDate").toString(),
						results.getString("validityTime").toString(), results.getString("seatReservation"), results.getString("ageGroup"), 
						results.getDouble("price"), results.getBoolean("isUsed"));
				
				Boolean isValidTicket = tkt.isTicketValid(tkt);
				
				if(isValidTicket)
				{											
					System.out.println("*-----------------------------*");
					System.out.println("* TICKET VALID!");
					System.out.println("*-----------------------------*");
					System.out.println("* OPENING BARRIERS AT " + _reader.getStationName().toUpperCase());
					System.out.println("*-----------------------------*\n");
				}
				else
				{
					System.out.println("*-----------------------------*");
					System.out.println("* TICKET INVALID!");
					System.out.println("*-----------------------------*");
					System.out.println("* " + tkt.getErrorMessage());
					System.out.println("*-----------------------------*");
				}
				
				if (tkt.getTicketType().contains("Advance"))
				{
					if(!tkt.getIsUsed() && tkt.getToStation().equals(_reader.getStationName()))
					{
						String updateQuery = "UPDATE ticket SET isUsed=1 WHERE ticketID=" + tkt.getTicketID() + ";";
						this.updateDatabase(updateQuery);
					}
				}
			}
		}
		results.close();
	}
	
	private void processCardInConductorMode() throws SQLException 
	{
		Vector<Ticket> passengerTickets = new Vector<Ticket>();	
		_dtControl = new DateTimeController();
		String dateToday = _dtControl.getTodaysDate();
		
		/*ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() 
		+ " AND serviceID=" + _reader.getServiceID() + " AND validityDate=NOW();");*/
		ResultSet results = queryDatabase("SELECT * FROM ticket WHERE cardID=" + _card.getID() + " AND validityDate='" + dateToday + "';");
		
		String ticketID_str = null;
		int ticketID_int = 0;
		
		if (!results.isBeforeFirst()) 
		{    
			printInfoMismatchMessage();
		}
		else
		{
			while(results.next())
			{
				Ticket tkt = new Ticket (results.getInt("ticketID"), results.getInt("cardID"), results.getInt("serviceID"),
						results.getString("validFromStation"), results.getString("validToStation"), results.getString("ticketType"), results.getString("class"), 
						results.getString("purchaseDateTime").toString(), results.getString("validityDate").toString(),
						results.getString("validityTime").toString(), results.getString("seatReservation"), results.getString("ageGroup"), 
						results.getDouble("price"), results.getBoolean("isUsed"));
				
				passengerTickets.addElement(tkt);
				
				/*ticketID_str = results.getString("serviceID");
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
				}*/
			}			
			Enumeration<Ticket> vEnum = passengerTickets.elements();
		      System.out.println("\nElements in vector:");
		      while(vEnum.hasMoreElements())
		         System.out.print(vEnum.nextElement().getTicketID() + " ");
			
			//System.out.println(passengerTickets);
			
		}
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