
import java.io.*;
import java.util.Scanner;

public class main 
{
	final static String DATA_FILE = "D:/Eclipse/RailSmartCard/tmp/data";
	final static String STATIONS_FILE = "D:/Eclipse/RailSmartCard/bin/Stations.txt";
	
	public static void main(String[] args)
	{
		CardReader cReader = new CardReader();
		Boolean isMessageDisplayed = false;
		
		while(true)	
		{
			if(!isMessageDisplayed)
			{
				System.out.println("Please present card");
				isMessageDisplayed = true;
			}
			cReader.systemPause();			
			if (cReader.doesIDFileExist())
			{			
				String ID = cReader.retrieveCardID(cReader.readIDFile());			
				if (ID != "-1")
				{
					System.out.println("ID = " + ID);				
					isMessageDisplayed = false;
					System.out.println("");
				}
				cReader.deleteFile();	
			}
		}
	}
	
	private static void displayMainMenu()
	{
		System.out.println("Welcome to Rail Smartcard System");
		System.out.println("Please select which mode to run system in:");
		System.out.println("1 - Station");
		System.out.println("2 - Conductor");
		System.out.println("0 - Quit");
	}
	
	private static int selectOperatingMode()
	{
		int choice = -1;
		
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(System.in);
			
			while(choice > 2 || choice < 0)
			{	
				System.out.print("\nOption: ");
				choice = scanner.nextInt();
			}
		}
		finally
		{
			scanner.close();
		}
		
		return choice;
	}
	
	private static void displayStationMenu()
	{
		System.out.println("Please insert the station code (or 0 to return to main menu)");
		System.out.println("Station code: ");
		selectStationCode();
	}
	
	private static String selectStationCode()
	{
		int choice = -1;
		
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(System.in);
			
			while(choice > 2 || choice < 0)
			{	
				System.out.print("\nOption: ");
				choice = scanner.nextInt();
			}
		}
		finally
		{
			scanner.close();
		}
		
		return "";
	}
	
	private static String searchStationFile()
	{
		String data = null;
		FileReader fReader = null;
		try
		{
			fReader = new FileReader(STATIONS_FILE);
			BufferedReader reader = new BufferedReader(fReader);	
			data = reader.readLine();
			fReader.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	
// 	private static Boolean doesIDFileExist()
//	{
//		File file = new File(DATA_FILE);
//		return file.exists();
//	}
//	
//	private static String readIDFile() 
//	{
//		String data = null;
//		FileReader fReader = null;
//		try
//		{
//			fReader = new FileReader(DATA_FILE);
//			BufferedReader reader = new BufferedReader(fReader);	
//			data = reader.readLine();
//			fReader.close();
//		} 
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		return data;
//	}
//	
//	private static String retrieveCardID(String data)
//	{
//		int start = data.indexOf("Text");
//		int end = data.indexOf("EncodingType");
//		
//		if((start != -1) || (end != -1))
//		{
//			return data.substring(start+7, end-3);
//		}
//		else
//		{
//			return "-1";
//		}
//	}
//	
//	private static void deleteFile()
//	{
//		File toBeDeleted = new File(DATA_FILE);
//		toBeDeleted.delete();
//		waitOneSecond();
//	}
//	
//	private static void waitOneSecond()
//	{
//		try
//		{
//			Thread.sleep(1000);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}
