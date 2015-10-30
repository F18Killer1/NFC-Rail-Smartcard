import java.io.*;

public class CardReader 
{
	final private static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	
	CardReader()
	{
		
	}
	
	public Boolean doesIDFileExist()
	{
		File file = new File(DATA_FILE);
		return file.exists();
	}
	
	public String readIDFile() 
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
	
	public String retrieveCardID(String data)
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
	
	public void deleteFile()
	{
		File toBeDeleted = new File(DATA_FILE);
		toBeDeleted.delete();
		systemPause();
	}
	
	public void systemPause()
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


 
}
