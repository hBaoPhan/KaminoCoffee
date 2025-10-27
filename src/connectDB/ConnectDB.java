package connectDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	public static void main(String[] args) {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYQUANCOFFEEJCD;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "sapassword";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
