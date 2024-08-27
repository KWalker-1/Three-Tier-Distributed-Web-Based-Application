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

@WebServlet("/PartsInsertServlet")
public class PartsInsertServlet extends HttpServlet {
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
        String pnum = request.getParameter("pnum");
        String pname = request.getParameter("pname");
        String color = request.getParameter("color");
        String weight = request.getParameter("weight");
        String city = request.getParameter("city");

        // Debug output
        System.out.println("Received values:");
        System.out.println("pnum: " + pnum);
        System.out.println("pname: " + pname);
        System.out.println("color: " + color);
        System.out.println("weight: " + weight);
        System.out.println("city: " + city);

        if (pnum == null || pname == null || color == null || weight == null || city == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }

        int weightParse;
        try {
            weightParse = Integer.parseInt(weight);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value");
            return;
        }

        String insertQuery = "INSERT INTO parts (pnum, pname, color, weight, city) VALUES (?, ?, ?, ?, ?)";
        String resultMessage;

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, pnum);
            pstmt.setString(2, pname);
            pstmt.setString(3, color);
            pstmt.setInt(4, weightParse);
            pstmt.setString(5, city);

            int rowsAffected = pstmt.executeUpdate();
            resultMessage = "Inserted " + rowsAffected + " row(s) with values: pnum=" + pnum + ", pname=" + pname + ", color=" + color + ", weight=" + weight +", city=" + city;
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
