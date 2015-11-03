
public class StationReader extends CardReader
{
	private String _STATION_NAME_;
	
	StationReader(String stationName)
	{
		super();
		_STATION_NAME_ = stationName;
		readyStationReader();
	}
	
	private void printStartupMessages()
	{
		System.out.println("\nBoot sequence\t\t[ SUCCESS ]");
		System.out.println("SYSTEM RUNNING IN STATION MODE @ ** " + _STATION_NAME_.toUpperCase() + " **");
	}
	
	private void readyStationReader()
	{
		printStartupMessages();
		super.run();
	}
}