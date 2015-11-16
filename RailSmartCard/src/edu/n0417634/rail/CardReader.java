package edu.n0417634.rail;

import java.io.*;
import java.sql.SQLException;

public class CardReader
{
	final private static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	
	private String _stationName;
	private int _serviceID;
	private String _mode;
	
	private DatabaseManager dbm;
	private Boolean _isMessageDisplayed;
	private Boolean _isRunning;
	
	CardReader()
	{
		this._stationName = null;
		this._serviceID =0;
		this._mode = null;
	}
	
	CardReader(String stationName)
	{
		this._stationName = stationName;
		this._serviceID =0;
		this._mode = "STATION";
		
		//System.out.println("Boot sequence\t\t[ SUCCESS ]");
		System.out.println("SYSTEM RUNNING IN STATION MODE AT ** " + this._stationName.toUpperCase() + " **");
	}
	
	CardReader(int serviceID)
	{
		this._serviceID = serviceID;
		this._stationName = null;
		this._mode = "CONDUCTOR";
		
		//System.out.println("Boot sequence\t\t[ SUCCESS ]");
		System.out.println("SYSTEM RUNNING IN CONDUCTOR MODE ON SERVICE ID: " + this._serviceID );
	}
	
	public void run() throws SQLException
	{
		dbm = new DatabaseManager();
		_isRunning = true;
		_isMessageDisplayed = false;
		resetMachine();
		
		dbm.init();
		
		while(_isRunning)
		{
			if(!_isMessageDisplayed)
			{
				System.out.println("Please present card");
				_isMessageDisplayed = true;
			}
			systemPause();
			/*if (doesIDFileExist()) 
			{
				Boolean cardValidated = validateCardID(retrieveCardID(readIDFile())); 
				
				if (cardValidated) 
				{ 
					String id_str = retrieveCardID(readIDFile()); */
					String id_str = "94"; //UNCOMMENT
													
					if (id_str.equals("SHUTDOWN"))
					{
						resetMachine();
						_isRunning = false;
					}
					else
					{
						int id_int = Integer.parseInt(id_str);
						dbm.performDatabaseOperations(new Card(id_int), this);
						
						break; //DEBUG
					}
				//} //C
				/*else //C
				{ //C
					System.out.println("Card unable to be validated, please try again or seek assistance!\n"); //C
				} //C*/
				resetMachine(); 
			//} 
		}
		
		//System.out.println("Executing SHUTDOWN procedures...");
		dbm.closeDBConnection();
		//System.out.println("-> Terminating all services\t\t[ SUCCESS ]");
		System.exit(0);
	}
	
	public String getReaderMode()
	{
		return _mode;
	}
	
	public int getServiceID()
	{
		return _serviceID;
	}
	
	public String getStationName()
	{
		return _stationName;
	}
	
	private Boolean doesIDFileExist()
	{
		File file = new File(DATA_FILE);
		return file.exists();
	}
	
	private String readIDFile() 
	{
		String data = null;
		FileReader fReader = null;
		try
		{
			fReader = new FileReader(DATA_FILE);
			BufferedReader reader = new BufferedReader(fReader);	
			data = reader.readLine();
			fReader.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	private String retrieveCardID(String data)
	{
		int start = data.indexOf("Text");
		int end = data.indexOf("EncodingType");
		
		if((start != -1) || (end != -1))
		{
			return data.substring(start+7, end-3);
		}
		else
		{
			return "-1";
		}
	}
	
	private void deleteFile()
	{
		File toBeDeleted = new File(DATA_FILE);
		toBeDeleted.delete();
		systemPause();
	}
	
	private void systemPause()
	{
		try
		{
			Thread.sleep(500);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private Boolean validateCardID(String ID)
	{
		return (ID != "-1");
	}
	
	private void resetMachine()
	{
		_isMessageDisplayed = false;
		deleteFile();
	}
}
