package edu.n0417634.rail;

public class Ticket 
{
	private int _ticketID, _cardID, _serviceID;
	private String _validFromStation, _validToStation, _ticketType, _ticketClass, _purchaseDate, _validityDate, _validityTime, _seatReservation, _ageGroup;
	private double _price; 
	private Boolean _isUsed;
	
	Ticket()
	{
		_ticketID = 0;
		_cardID = 0;
		_serviceID = 0;
		_validFromStation = null; 
		_validToStation = null; 
		_ticketType = null; 
		_ticketClass = null; 
		_purchaseDate = null; 
		_validityDate = null; 
		_validityTime = null;
		_seatReservation = null; 
		_ageGroup = null;
		_isUsed = false;		
	}
	
	Ticket(int tID, int cID, int sID, String from, String to, String type, String tClass, String pDate, 
			String vDate, String vTime, String seat, String age, double price, Boolean isUsed)
	{
		_ticketID = tID;
		_cardID = cID;
		_serviceID = sID;
		_validFromStation = from; 
		_validToStation = to; 
		_ticketType = type; 
		_ticketClass = tClass; 
		_purchaseDate = pDate; 
		_validityDate = vDate; 
		_validityTime = vTime;
		_seatReservation = seat; 
		_ageGroup = age;
		_isUsed = isUsed;		
	}
}
