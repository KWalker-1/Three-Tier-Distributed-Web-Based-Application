import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DBTest {

    public static void main(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();
        FileInputStream filein = null;

        try {
            // Load properties
            filein = new FileInputStream("/Library/Tomcat10125/webapps/Project-3/WEB-INF/lib/systemapp.properties");
            properties.load(filein);

            // Setup data source
            String url = properties.getProperty("MYSQL_DB_URL");
            String user = properties.getProperty("MYSQL_DB_USERNAME");
            String password = properties.getProperty("MYSQL_DB_PASSWORD");
            
            System.out.println(url);
            System.out.println(user);
            System.out.println(password);

            // Establish connection
            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (filein != null) {
                try {
                    filein.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
