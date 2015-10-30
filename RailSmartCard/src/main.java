import java.util.Scanner;

public class main 
{
	public static void main(String[] args)
	{
		CardReader cReader;
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter operating mode (1:Station, 2:Conductor): ");
		int mode = scanner.nextInt();
		int serviceID = 0;
		String stationName = null;
		
		if(mode == 1)
		{
			System.out.print("Enter station name: ");
			stationName = scanner.next();
			cReader = new CardReader(mode, stationName);
		}
		else
		{
			System.out.print("Enter service ID: ");
			serviceID = scanner.nextInt();
			cReader = new CardReader(mode, serviceID);
		}
		scanner.close();
		cReader.run();
	}	
}