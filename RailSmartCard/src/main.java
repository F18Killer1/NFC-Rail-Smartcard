import java.util.Scanner;

public class main 
{
	public static void main(String[] args)
	{
		CardReader cReader = null;
		
		/*Scanner scanner = new Scanner(System.in);
		System.out.print("Enter operating mode (1:Station, 2:Conductor): ");
		int mode = scanner.nextInt();
		int serviceID = 0;
		String stationName = null;
		
		switch (mode)
		{
		case 1: System.out.print("Enter station name: ");
				stationName = scanner.next();
				cReader = new CardReader(mode, stationName);
				break;
				
		case 2: System.out.print("Enter service ID: ");
				serviceID = scanner.nextInt();
				cReader = new CardReader(mode, serviceID);
				break;
				
		default: System.out.print("Invalid option, exiting...");
				 System.exit(1);
		}
		
		scanner.close();*/
		
		//DEBUG CODE
		cReader = new CardReader(3, "Test");
		//END DEBUG CODE
		
		cReader.run();
	}	
}