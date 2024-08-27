import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@WebServlet("/ShipmentsInsertServlet")
public class ShipmentsInsertServlet extends HttpServlet {
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
        String pnum = request.getParameter("pnum");
        String jnum = request.getParameter("jnum");
        String quantity = request.getParameter("quantity");

        // Debug output
        System.out.println("Received values:");
        System.out.println("snum: " + snum);
        System.out.println("pnum: " + pnum);
        System.out.println("jnum: " + jnum);
        System.out.println("quantity: " + quantity);

        if (snum == null || pnum == null || jnum == null || quantity == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }

        int quantityParse;
        try {
            quantityParse = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity value");
            return;
        }

        String insertQuery = "INSERT INTO shipments (snum, pnum, jnum, quantity) VALUES (?, ?, ?, ?)";
        StringBuilder resultMessage = new StringBuilder();

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, snum);
            pstmt.setString(2, pnum);
            pstmt.setString(3, jnum);
            pstmt.setInt(4, quantityParse);

            int rowsAffected = pstmt.executeUpdate();
            resultMessage.append("Inserted ").append(rowsAffected).append(" row(s) with values: snum=").append(snum)
                    .append(", pnum=").append(pnum).append(", jnum=").append(jnum).append(", quantity=").append(quantityParse);

            // Business logic to check quantities and update supplier status
            if (quantityParse >= 100) {
                triggerBusinessLogic(resultMessage);
            }
        } catch (SQLException e) {
            resultMessage.append("Error during insertion: ").append(e.getMessage());
        }

        request.setAttribute("executionResults", resultMessage.toString());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/DataEntry.jsp"); // Change to your JSP page's path
        dispatcher.forward(request, response);
    }

    private void triggerBusinessLogic(StringBuilder result) throws SQLException {
        Statement statement = connection.createStatement();
        // Check for quantities >= 100 in the `shipments` table
        ResultSet rs = statement.executeQuery("SELECT DISTINCT snum FROM shipments WHERE quantity >= 100");
        if (rs.next()) {
            // Increment the status of suppliers
            String updateStatusQuery = "UPDATE suppliers SET status = status + 5 WHERE snum IN (SELECT DISTINCT snum FROM shipments WHERE quantity >= 100)";
            int rowsAffected = statement.executeUpdate(updateStatusQuery);
            result.append("<p>Business logic triggered: Supplier status incremented by 5 for ").append(rowsAffected).append(" suppliers.</p>");
        }
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
