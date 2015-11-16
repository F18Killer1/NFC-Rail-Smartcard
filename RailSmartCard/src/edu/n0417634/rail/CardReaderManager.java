package edu.n0417634.rail;
import java.sql.SQLException;
import java.util.Scanner;


public class CardReaderManager 
{
	CardReaderManager() {}	
	
	public void start() throws SQLException
	{
		//System.out.println("Initiating boot sequence...\n");

		//***** DEBUG CODE ********
		//new StationReader("nottingham");
		new CardReader("London Kings Cross").run();
		//new CardReader(1069).run();
		//**** END DEBUG CODE *****
		
		/*Scanner scanner = new Scanner(System.in);
		System.out.print("-> Enter operating mode (1:Station, 2:Conductor): ");
		int mode = scanner.nextInt();
		int serviceID = 0;
		String stationName = null;
		
		switch (mode)
		{
		case 1: System.out.print("--> Enter station name: ");
				stationName = scanner.next();
				new CardReader(stationName).run();
				break;
				
		case 2: System.out.print("--> Enter service ID: ");
				serviceID = scanner.nextInt();
				new CardReader(serviceID).run();
				break;
				
		default: System.out.print("Invalid option, exiting...");
				 System.exit(1);
		}
		scanner.close();*/
	}
}
