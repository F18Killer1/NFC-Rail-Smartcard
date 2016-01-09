package edu.n0417634.rail;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeController 
{
	private Calendar _calendar;
	private String _todaysDate;
	
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
		String day_str = Integer.toString(day);
		String mon_str = Integer.toString(month);
				
		if(day < 10) 
		{ 
			day_str = "0" + day;
		}
		
		if(month < 10) 
		{ 
			mon_str = "0" + month; 
		}
		_todaysDate = year + "-" + mon_str + "-" + day_str;		
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
		return Integer.parseInt(Integer.toString(hoursNow) + Integer.parseInt(Integer.toString(minutesNow)));
	}
}
