
import java.io.*;

public class main 
{
	final static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	
	public static void main(String[] args)
	{
		String ID = null;
		Boolean isMessageDisplayed = false;
		
		while(true)
		{
			if(!isMessageDisplayed)
			{
				System.out.println("Please present card");
				isMessageDisplayed = true;
			}
			waitOneSecond();			
			if (doesIDFileExist())
			{			
				ID = retrieveCardID(readIDFile());
				
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
	
 	private static Boolean doesIDFileExist()
	{
		File file = new File(DATA_FILE);
		return file.exists();
	}
	
	private static String readIDFile() 
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
	
	private static String retrieveCardID(String data)
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
	
	private static void deleteFile()
	{
		File toBeDeleted = new File(DATA_FILE);
		toBeDeleted.delete();
		waitOneSecond();
	}
	
	private static void waitOneSecond()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
