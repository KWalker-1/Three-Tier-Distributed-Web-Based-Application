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

@WebServlet("/SuppliersInsertServlet")
public class SuppliersInsertServlet extends HttpServlet {
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
        String snum = request.getParameter("snum");
        String sname = request.getParameter("sname");
        String statusStr = request.getParameter("status");
        String city = request.getParameter("city");

        // Debug output
        System.out.println("Received values:");
        System.out.println("snum: " + snum);
        System.out.println("sname: " + sname);
        System.out.println("status: " + statusStr);
        System.out.println("city: " + city);

        if (snum == null || sname == null || statusStr == null || city == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }

        int status;
        try {
            status = Integer.parseInt(statusStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid status value");
            return;
        }

        String insertQuery = "INSERT INTO suppliers (snum, sname, status, city) VALUES (?, ?, ?, ?)";
        String resultMessage;

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, snum);
            pstmt.setString(2, sname);
            pstmt.setInt(3, status);
            pstmt.setString(4, city);

            int rowsAffected = pstmt.executeUpdate();
            resultMessage = "Inserted " + rowsAffected + " row(s) with values: snum=" + snum + ", sname=" + sname + ", status=" + status + ", city=" + city;
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
