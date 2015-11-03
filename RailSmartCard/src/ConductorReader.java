
public class ConductorReader extends CardReader
{
	private int _SERVICE_ID_;
	
	ConductorReader(int serviceID)
	{
		super();
		_SERVICE_ID_ = serviceID;
		readyConductorReader();
	}
	
	private void printStartupMessages()
	{
		System.out.println("\nBoot sequence\t\t[ SUCCESS ]");
		System.out.println("SYSTEM RUNNING IN CONDUCTOR MODE ON SERVICE ID: " + _SERVICE_ID_);
	}
	
	private void readyConductorReader()
	{
		printStartupMessages();
		super.run();
	}
}