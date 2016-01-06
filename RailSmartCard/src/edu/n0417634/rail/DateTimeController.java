package edu.n0417634.rail;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeController 
{
	private Calendar _calendar;
	private String _todaysDate;
	private String _timeNow;
	
	DateTimeController()
	{
		_calendar = new GregorianCalendar();
		setTodaysDate();
	}
	
	private void setTodaysDate()
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
	
	public String getTodaysDate()
	{
		return _todaysDate;
	}
	
	public int getTimeNow() 
	{
		int hoursNow = _calendar.get(Calendar.HOUR_OF_DAY);
		int minutesNow = _calendar.get(Calendar.MINUTE);
		
		if(minutesNow < 10)		
		{
			return Integer.parseInt(Integer.toString(hoursNow) + "0" + Integer.parseInt(Integer.toString(minutesNow)));
		}			
		return Integer.parseInt(Integer.toString(hoursNow) + "0" + Integer.parseInt(Integer.toString(minutesNow)));
	}
}
