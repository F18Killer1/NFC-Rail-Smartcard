import java.util.*;

public class CardReaderManager 
{
	CardReaderManager()
	{
		this.run();
	}
	
	private void run()
	{
		
		//***** DEBUG CODE ********
		//new StationReader("nottingham");
		new ConductorReader(69);
		//**** END DEBUG CODE *****
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Initiating boot sequence...\n");
		System.out.print("-> Enter operating mode (1:Station, 2:Conductor): ");
		int mode = scanner.nextInt();
		int serviceID = 0;
		String stationName = null;
		
		switch (mode)
		{
		case 1: System.out.print("--> Enter station name: ");
				stationName = scanner.next();
				new StationReader(stationName);
				break;
				
		case 2: System.out.print("--> Enter service ID: ");
				serviceID = scanner.nextInt();
				new ConductorReader(serviceID);
				break;
				
		default: System.out.print("Invalid option, exiting...");
				 System.exit(1);
		}
		scanner.close();
	}
}
