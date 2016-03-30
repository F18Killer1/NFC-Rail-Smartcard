package edu.n0417634.rail;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class CardReaderManager 
{
	final private static String stationsFile = "stations.txt";
	final private static String servicesFile = "services.txt";
	private ArrayList<String> _stations = new ArrayList();
	private ArrayList<String> _services = new ArrayList();
	BufferedReader b_reader;
	String line;
	int verifyCounter;
	int colonPos;
	
	CardReaderManager() {}	
	
	public void start() throws SQLException
	{	
		Scanner scanner = new Scanner(System.in);
		System.out.print("-> Enter operating mode (1:Station, 2:Conductor): ");
		int mode = scanner.nextInt();
		
		switch (mode)
		{
		case 1: 				
				for(int i=0;	i<_stations.size();	i++)
				{
					System.out.println("--> " + _stations.get(i));
				}
				
				System.out.print("\n--> Select station: \n");
				int stationInt = scanner.nextInt();
				Boolean validStation = validateStationID(stationInt);
								
				if(validStation)
				{
					new CardReader(getStationName(stationInt)).run();
				}
				else
				{
					throwExit();
				}
				
		case 2: 		
				for(int i=0;	i<_services.size();	i++)
				{
					System.out.println("--> " + _services.get(i));
				}
				
				System.out.print("\n--> Enter service ID: \n");
				int serviceInt = scanner.nextInt();
				Boolean validService = validateServiceID(serviceInt);

				if(validService)
				{
					new CardReader(serviceInt).run();
				}
				else
				{
					throwExit();
				}
				
		default: throwExit();
		}
		scanner.close();
	}
	
	private Boolean validateStationID(int station)
	{
		for(int i=0;	i<_stations.size();	i++)
		{
			colonPos = _stations.get(i).indexOf(":");
			if(_stations.get(i).substring(0, colonPos).equals( Integer.toString(station)))
			{
				return true;
			}
		}
		return false;
	}
	
	private Boolean validateServiceID(int service)
	{
		for(int i=0;	i<_services.size();	i++)
		{
			colonPos = _services.get(i).indexOf(":");
			if(_services.get(i).substring(0, colonPos).equals( Integer.toString(service)))
			{
				return true;
			}
		}
		return false;
	}
	
	public Boolean initialise()
	{
		Boolean stationsSuccess = populateStations();
		Boolean servicesSuccess = populateServices();
		
		if(stationsSuccess && servicesSuccess)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private Boolean populateStations()
	{
		verifyCounter = 0;
		try 
        {
        	b_reader = new BufferedReader(new FileReader(stationsFile));
            try 
            {
                while ((line = b_reader.readLine()) != null ) 
                {
                	_stations.add(line);
                	verifyCounter++;
                } 
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        
        if(verifyCounter == _stations.size())
        {
        	return true;
        }
		return false;
	}
	
	private Boolean populateServices()
	{
		verifyCounter = 0;
		try 
        {
        	b_reader = new BufferedReader(new FileReader(servicesFile));
            try 
            {
                while ((line = b_reader.readLine()) != null ) 
                {
                	_services.add(line);
                	verifyCounter++;
                } 
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        
        if(verifyCounter == _services.size())
        {
        	return true;
        }
		return false;
	}
	
	private String getStationName(int mode)
	{
		switch (mode)
		{
		case 1: return "Birmingham New St"; 
		case 2: return "Derby";
		case 3: return "Nottingham";
		default: System.out.print("Fatal error occurred, exiting..."); System.exit(1);
		}
		return "";
	} 
	
	private void throwExit()
	{
		System.out.print("Invalid option, exiting...");
		System.exit(1);
	}
}
