import java.io.*;

public class CardReader 
{
	final private static String DATA_FILE = "tmp/data";
	
	DatabaseManager dbm;
	private Boolean _PRESENT_CARD_MSG_ = false;
	
	CardReader()
	{
		
	}
	
	protected Boolean doesIDFileExist()
	{
		File file = new File(DATA_FILE);
		return file.exists();
	}
	
	protected String readIDFile() 
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
	
	protected String retrieveCardID(String data)
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
	
	protected void deleteFile()
	{
		File toBeDeleted = new File(DATA_FILE);
		toBeDeleted.delete();
		systemPause();
	}
	
	protected void systemPause()
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
	
	protected void resetMachine()
	{
		_PRESENT_CARD_MSG_ = false;
		deleteFile();
	}
	
	public void run()
	{
		dbm = new DatabaseManager();
	
		resetMachine();			//Enable clean file data on start-up
		while(true)	
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
					String id_str = retrieveCardID(readIDFile());
													
					if (id_str.equals("SHUTDOWN"))
					{
						resetMachine();
						break;
					}
					else
					{
						//System.out.println("ID = " + id + "\n");
						int id_int = Integer.parseInt(id_str);
						dbm.performDatabaseOperations(new Card(id_int));
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
		System.out.println("-> Terminating all services\t\t[ SUCCESS ]");
		System.exit(0);
	}
}