import java.io.*;
import java.sql.*;

public class CardReader 
{
	final private static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	
	private int _MODE_ = 0;	
	private String _STATION_NAME_;
	private int _SERVICE_ID_;
	private Boolean _PRESENT_CARD_MSG_ = false;
	
	CardReader()
	{
		
	}
	
	CardReader(int mode, String stationName)
	{
		_MODE_ = mode;
		_STATION_NAME_ = stationName;
		_SERVICE_ID_ = 0;
		System.out.println("Intitialising boot sequence...");
	}
	
	CardReader(int mode, int serviceID)
	{
		_MODE_ = mode;
		_STATION_NAME_ = null;
		_SERVICE_ID_ = serviceID;
		System.out.println("Intitialising boot sequence...");
	}
		
	private String getMode()
	{
		//DEBUG
		if(_MODE_ == 3) { return "TEST"; }
		// END DEBUG
		
		if(_MODE_ == 1)
		{
			return "STATION";
		}
		return "CONDUCTOR";
	}
	
	private String getWelcomeMessage()
	{
		//DEBUG
		if(_MODE_ == 3) { return "SYSTEM RUNNING IN TEST MODE"; }
		// END DEBUG
		
		if(_MODE_ == 1)
		{
			return "THIS TERMINAL IS RUNNING AT " + _STATION_NAME_.toUpperCase() + " STATION";
		}
		return "THIS TERMINAL IS OPERATING ON SERVICE ID: " + _SERVICE_ID_;
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
		_PRESENT_CARD_MSG_ = false;
		deleteFile();
	}
	
	public void run()
	{
		DatabaseManager dbm = new DatabaseManager();
		System.out.println("*** SYSTEM RUNNING IN " + getMode() + " MODE ***");
		System.out.println("*** " + getWelcomeMessage() +" ***\n");

		//System.exit(1);
		
		resetMachine();			//Enable clean file data on start-up
		while(_MODE_ != 0)	
		{	
			if(!_PRESENT_CARD_MSG_)
			{
				System.out.println("Please present card");
				_PRESENT_CARD_MSG_ = true;
			}
			systemPause();			
			if (doesIDFileExist())
			{			
				Boolean cardValidated = validateCardID(retrieveCardID(readIDFile()));
				
				if (cardValidated)
				{
					Card card = new Card(retrieveCardID(readIDFile()));
					
					if (card.getCardID().equals("SHUTDOWN"))
					{
						resetMachine();
						break;
					}
					else
					{
						System.out.println("ID = " + card.getCardID() + "\n");
					}
				}
				else
				{
					System.out.println("Card unable to be validated, please try again or seek assistance!\n");
				}
				resetMachine();
			}
		}
		System.out.println("Executing SHUTDOWN procedures...");
		dbm.closeDBConnection();
	}
}