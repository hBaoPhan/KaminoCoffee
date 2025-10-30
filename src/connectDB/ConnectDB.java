package connectDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	
	public static void connect() {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYKAMINOCOFFEE;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "sapassword";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args) {
		connect();
    }

}
