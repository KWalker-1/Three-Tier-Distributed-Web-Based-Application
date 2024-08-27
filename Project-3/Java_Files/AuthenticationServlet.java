import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

public class AuthenticationServlet extends HttpServlet {

    private Connection connection; // Connection object used to connect to MySQL DBMS
    private ResultSet lookupResults; // ResultSet object holds response from search of the usercredentials table
    private PreparedStatement pstatement; // Prepared Statement object

    // Process 'post' request from client
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inBoundUserName = request.getParameter("username"); // user submitted username
        String inBoundPassword = request.getParameter("password"); // user submitted password

        String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";

        boolean userCredentialsOK = false;

        try {
            // get a connection to the credentialsDB
            getDBConnection();
            // set the prepared statement object
            pstatement = connection.prepareStatement(credentialsSearchQuery);
            // set prepared statement parameters
            pstatement.setString(1, inBoundUserName);
            pstatement.setString(2, inBoundPassword);
            // see if user entered credentials appear in the usercredentials table in the credentialsDB
            lookupResults = pstatement.executeQuery();

            if (lookupResults.next()) {
                userCredentialsOK = true;
            } else {
                userCredentialsOK = false;
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        //Redirect user to correct home page
        try {
            if (userCredentialsOK) {
                switch (inBoundUserName) {
                    case "client":
                        redirectUser(response, "ClientUserApp.jsp");
                        break;
                    case "root":
                        redirectUser(response, "rootHome.jsp");
                        break;
                    case "dataentryuser":
                        redirectUser(response, "DataEntry.jsp");
                        break;
                    case "theaccountant":
                        redirectUser(response, "AccountantUserApp.jsp");
                        break;
                    default:
                        redirectUser(response, "errorpage.html");
                        break;
                }
            } else {
                redirectUser(response, "errorpage.html");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // end doPost() method

    private void getDBConnection() {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        try {
            // Read properties file
            filein = new FileInputStream("/Library/Tomcat10125/webapps/Project-3/WEB-INF/lib/systemapp.properties");
            properties.load(filein);

            // Setup data source
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            // Establish connection
            connection = dataSource.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (filein != null) {
                try {
                    filein.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } // end getDBConnection() method
    
    //method used to redirect user to their home page
    private void redirectUser(HttpServletResponse response, String url) throws IOException {
        System.out.println("Redirecting to " + url);
        response.sendRedirect(url);
    }
}
