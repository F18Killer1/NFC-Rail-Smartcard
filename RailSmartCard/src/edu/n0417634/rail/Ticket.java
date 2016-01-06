package edu.n0417634.rail;

public class Ticket 
{
	private int _ticketID, _cardID, _serviceID;
	private String _validFromStation, _validToStation, _ticketType, _ticketClass, _purchaseDate, _validityDate, _validityTime, _seatReservation, _ageGroup;
	private double _price; 
	private Boolean _isUsed;
	private String _errorMessage;
	
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
		_errorMessage = null;
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
		_errorMessage = null;
	}
	
	public Boolean isTicketValid(Ticket tkt)
	{		
		if(this.getTicketType().contains("Advance"))
		{
			int timeNow = new DateTimeController().getTimeNow();
			int ticketTime = this.stripColon(this.getValidityTime());
			int timeDiff = timeNow - ticketTime;		
						
			if(timeDiff > 0)
			{
				_errorMessage = "Ticket expired on " + this.formatDate(this.getValidityDate()) + " at " + this.stripSeconds(this.getValidityTime());
				return false;
			}
			else if (timeDiff < -100)
			{
				_errorMessage = "Ticket valid on " + this.formatDate(this.getValidityDate()) +  " between " + this.getValidityStartTime(this.getValidityTime()) 
													+ " and " + this.stripSeconds(this.getValidityTime());
				return false;
			}
		}
		return true;
	}
	
	private String formatDate(String date)
	{
		String[] dates = date.split("-");
		return dates[2] + "/" + dates[1] + "/" + dates[0];		
	}
		
	private String getValidityStartTime(String time)
	{
		String[] times = time.split(":");
		return (Integer.parseInt(times[0]) -1) + ":" + times[1];
	}
	
	private int stripColon(String time)
	{
		String[] times = time.split(":");
		return Integer.parseInt(times[0] + "" +times[1]); 
	}
	
	private String stripSeconds(String time)
	{
		String[] times = time.split(":");
		return times[0] + ":" + times[1];
	}	
	
	public String getFromStation()
	{
		return _validFromStation;
	}
	
	public String getToStation()
	{
		return _validToStation;
	}
	
	public String getTicketType()
	{
		return _ticketType;
	}
	
	public String getSeat()
	{
		return _seatReservation;
	}
	
	public String getTicketClass()
	{
		return _ticketClass;
	}
	
	private String getValidityTime()
	{
		return _validityTime;
	}
	
	public String getErrorMessage()
	{
		return _errorMessage;
	}
	
	public Boolean getIsUsed()
	{
		return _isUsed;
	}
	
	public int getTicketID()
	{
		return _ticketID;
	}
	
	public int getServiceID()
	{
		return _serviceID;
	}
	
	private String getValidityDate()
	{
		return _validityDate;
	}
}


