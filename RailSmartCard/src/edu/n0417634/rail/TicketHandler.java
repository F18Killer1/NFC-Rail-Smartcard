package edu.n0417634.rail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TicketHandler 
{
	private ArrayList<Ticket> _tickets = new ArrayList();
	private Boolean _isValidTicket = false;
	private CardReader _reader =  null;
	private int _position = 0;
	
	TicketHandler()
	{
		
	}
	
	TicketHandler(ResultSet rs, CardReader reader) throws SQLException
	{
		_reader = reader;
		populateResults(rs);
	}
	
	private void populateResults(ResultSet results) throws SQLException
	{
		while(results.next())
		{
			Ticket t = new Ticket (results.getInt("ticketID"), results.getInt("cardID"), results.getInt("serviceID"),
					results.getString("validFromStation"), results.getString("validToStation"), results.getString("ticketType"), results.getString("class"), 
					results.getString("purchaseDateTime").toString(), results.getString("validityDate").toString(),
					results.getString("validityTime").toString(), results.getString("seatReservation"), results.getString("ageGroup"), 
					results.getDouble("price"), results.getBoolean("isUsed"));
			
			_tickets.add(t);
			
		}
		
		if(_reader.getReaderMode() == "STATION")
		{
			checkValidStationTicket();
		}
		else
		{
			checkValidConductorTicket();
		}
	}
	
	private void checkValidConductorTicket() throws SQLException
	{
		for(int i=0;  i < _tickets.size();	i++)
		{
			if(!_isValidTicket)
			{
				if(_tickets.get(i).getServiceID() == _reader.getServiceID())
				{
					_position = i;
					_isValidTicket = _tickets.get(i).isTicketValid(_tickets.get(i), "conductor");
				}
				else
				{
					if(_tickets.get(i).getTicketType().equals("Off-Peak")) 
					{
						_position = i;
						_isValidTicket = _tickets.get(i).isTicketValid(_tickets.get(i), "conductor");
					}
					else 
					{
						_tickets.get(i).setErrorMessage("Ticket not valid for this service!");
					}
				}
			}
		}
		
		if(_isValidTicket)
		{		
			Ticket tkt = _tickets.get(_position);
			String tktType = tkt.getAgeGroup() + " " + tkt.getTicketType();
			
			if(_tickets.get(_position).getTicketType().equals("Off-Peak")) 
			{
				System.out.println("*-----------------------------*");
				System.out.println("* !!! -- CHECK TICKET -- !!!");
				System.out.println("*-----------------------------*");
				System.out.println("TYPE:\t" + tktType.toUpperCase());
				System.out.println("CLASS:\t" + tkt.getTicketClass());
				System.out.println("TRIP:\t" + tkt.getFromStation() + " -> " + tkt.getToStation());
				System.out.println("*-----------------------------*");
			}
			else
			{
				System.out.println("*-----------------------------*");
				System.out.println("* TICKET VALID!");
				System.out.println("*-----------------------------*");
				System.out.println("TYPE:\t" + tktType.toUpperCase());
				System.out.println("CLASS:\t" + tkt.getTicketClass());
				System.out.println("TRIP:\t" + tkt.getFromStation() + " -> " + tkt.getToStation());
				
				if(tktType.contains("Advance")) 
				{
					System.out.println("SEAT:\t" + tkt.getSeat());
				}
				System.out.println("*-----------------------------*");
				
				for(int i=0;  i < _tickets.size();	i++)
				{
					
					if(_tickets.get(i).getServiceID() == _reader.getServiceID())
					{
						System.out.println("--> " + _tickets.get(i).getFromStation() + " TO " + _tickets.get(i).getToStation());
					}
					else
					{
						if(tktType.contains(_tickets.get(i).getTicketType())) 
						{
							System.out.println(_tickets.get(i).getFromStation() + " TO " + _tickets.get(i).getToStation());
						}
					}
				}
				System.out.println("*-----------------------------*\n");
			}
		}
		else
		{
			System.out.println("*-----------------------------*");
			System.out.println("* TICKET INVALID!");
			System.out.println("*-----------------------------*");
			System.out.println("This is service: " + _reader.getServiceID());
			
			for(int i=0;  i < _tickets.size();	i++)
			{
				System.out.println("\nT#" + _tickets.get(i).getTicketID() + ", S#" + _tickets.get(i).getServiceID() + " " +_tickets.get(i).getFromStation() + " TO " + _tickets.get(i).getToStation() +  
						" (" + _tickets.get(i).getTicketType() + ")");
				System.out.println(_tickets.get(i).getErrorMessage());
			
			}
			System.out.println("*-----------------------------*");
		}
		
	}
		
	private void checkValidStationTicket() throws SQLException
	{
		for(int i=0;  i < _tickets.size();	i++)
		{
			if(!_isValidTicket)
			{
				_position = i;
				_isValidTicket = _tickets.get(i).isTicketValid(_tickets.get(i), "station");
			}
		}
		
		if(_isValidTicket)
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
			System.out.println("* " + _tickets.get(_position).getErrorMessage());
			System.out.println("*-----------------------------*");
		}
		
		Ticket tkt = _tickets.get(_position);
			
		if(!tkt.getIsUsed() && tkt.getToStation().equals(_reader.getStationName()))
		{
			String updateQuery = "UPDATE ticket SET isUsed=1 WHERE ticketID=" + tkt.getTicketID() + ";";
			DatabaseManager.updateDatabase(updateQuery);
		}
		
	}
}