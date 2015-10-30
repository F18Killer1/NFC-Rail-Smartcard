import java.io.*;

public class CardReader 
{
	final private static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	
	private int _MODE_ = 0;	
	private String _STATION_NAME_;
	private int _SERVICE_ID_;
	
	CardReader()
	{
		
	}
	
	CardReader(int mode, String stationName)
	{
		_MODE_ = mode;
		_STATION_NAME_ = stationName;
		_SERVICE_ID_ = 0;
	}
	
	CardReader(int mode, int serviceID)
	{
		_MODE_ = mode;
		_STATION_NAME_ = null;
		_SERVICE_ID_ = serviceID;
	}
		
	private String getMode()
	{
		if(_MODE_ == 1)
		{
			return "STATION";
		}
		return "CONDUCTOR";
	}
	
	private String getWelcomeMessage()
	{
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
	
	public void run()
	{
		System.out.println("*** SYSTEM RUNNING IN " + getMode() + " MODE ***");
		System.out.println("*** " + getWelcomeMessage() +" ***\n");
		Boolean isMessageDisplayed = false;
		while(true)	
		{	
			if(!isMessageDisplayed)
			{
				System.out.println("Please present card");
				isMessageDisplayed = true;
			}
			systemPause();			
			if (doesIDFileExist())
			{			
				String ID = retrieveCardID(readIDFile());			
				if (ID != "-1")
				{
					System.out.println("ID = " + ID);				
					isMessageDisplayed = false;
					System.out.println("");
				}
				deleteFile();	
			}
		}		
	}
}
