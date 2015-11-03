import java.sql.*;

public class DatabaseManager 
{
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	final static String DB_URL = "jdbc:mysql://localhost:3306/electronicadvancetickets";
	final static String USER = "SYSTEM";
	final static String PASS = "5p5$Dmh_YAcA";
	Connection _CONNECTION_ = null;
	
	DatabaseManager()
	{
		connectToDB();
	}
	
	private void connectToDB()
	{
		try
		{
			_CONNECTION_ = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connection to database\t[ SUCCESS ]\n");
		}
		catch(SQLException sqlEx)
		{
			sqlEx.printStackTrace();
		}
	}
	
	public void closeDBConnection()
	{
		try
		{
			_CONNECTION_.close();
			System.out.println("-> Terminating DB connection\t\t[ SUCCESS ]");
		}
		catch(SQLException sqlEx)
		{
			sqlEx.printStackTrace();
		}
	}
	
	public void performDatabaseOperations(Card card)
	{
		System.out.println("ID of card: " + card.getID());
		String operation = calculateCardOperation(card.getID());
	}
	
	public String calculateCardOperation(int id)
	{
		return "";
	}
	
	public ResultSet queryDatabase(String query) throws SQLException
	{
		Statement statement = _CONNECTION_.createStatement();
		return statement.executeQuery(query);
	}
	
	/*			//Extract data from result set
			while(rs.next())
			{
				//Retrieve by column name
				int id  = rs.getInt("id");
				int age = rs.getInt("age");
				String first = rs.getString("first");
				String last = rs.getString("last");
				
				//Display values
				System.out.print("ID: " + id);
				System.out.print(", Age: " + age);
				System.out.print(", First: " + first);
				System.out.println(", Last: " + last);
				}
			} 
	}*/
}
