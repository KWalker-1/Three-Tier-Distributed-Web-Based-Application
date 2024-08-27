import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


@WebServlet("/JobsInsertServlet")
public class JobsInsertServlet extends HttpServlet{

	 private static final long serialVersionUID = 1L;

	    private Connection connection;

	    @Override
	    public void init() throws ServletException {
	        InputStream inputStream = null;
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            Properties properties = new Properties();
	            inputStream = getServletContext().getResourceAsStream("/WEB-INF/lib/dataentry.properties");

	            if (inputStream == null) {
	                throw new ServletException("Properties file not found");
	            }

	            properties.load(inputStream);
	            String url = properties.getProperty("MYSQL_DB_URL");
	            String username = properties.getProperty("MYSQL_DB_USERNAME");
	            String password = properties.getProperty("MYSQL_DB_PASSWORD");
	            connection = DriverManager.getConnection(url, username, password);
	        } catch (ClassNotFoundException e) {
	            throw new ServletException("MySQL JDBC Driver not found", e);
	        } catch (SQLException e) {
	            throw new ServletException("Database connection error", e);
	        } catch (IOException e) {
	            throw new ServletException("Properties file error", e);
	        } finally {
	            if (inputStream != null) {
	                try {
	                    inputStream.close();
	                } catch (IOException e) {
	                    throw new ServletException("Error closing properties file", e);
	                }
	            }
	        }
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String jnum = request.getParameter("jnum");
	        String jname = request.getParameter("jname");
	        String numworkers = request.getParameter("numworkers");
	        String city = request.getParameter("city");

	        // Debug output
	        System.out.println("Received values:");
	        System.out.println("jnum: " + jnum);
	        System.out.println("jname: " + jname);
	        System.out.println("numworkers: " + numworkers);
	        System.out.println("city: " + city);

	        if (jnum == null || jname == null || numworkers == null || city == null) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
	            return;
	        }

	        int numworkersParse;
	        try {
	            numworkersParse = Integer.parseInt(numworkers);
	        } catch (NumberFormatException e) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value");
	            return;
	        }

	        String insertQuery = "INSERT INTO jobs (jnum, jname, numworkers, city) VALUES (?, ?, ?, ?)";
	        String resultMessage;

	        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
	            pstmt.setString(1, jnum);
	            pstmt.setString(2, jname);
	            pstmt.setInt(3, numworkersParse);
	            pstmt.setString(4, city);

	            int rowsAffected = pstmt.executeUpdate();
	            resultMessage = "Inserted " + rowsAffected + " row(s) with values: jnum=" + jnum + ", jname=" + jname + ", numworkers=" + numworkers +", city=" + city;
	        } catch (SQLException e) {
	            resultMessage = "Error during insertion: " + e.getMessage();
	        }

	        request.setAttribute("executionResults", resultMessage);
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/DataEntry.jsp"); // Change to your JSP page's path
	        dispatcher.forward(request, response);
	    }

	    @Override
	    public void destroy() {
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

