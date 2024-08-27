
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class AccountantApp extends HttpServlet {

    private Connection connection;

    @Override
    public void init() throws ServletException {
        InputStream inputStream = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties properties = new Properties();
            inputStream = getServletContext().getResourceAsStream("/WEB-INF/lib/theaccountant.properties");

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
        String option = request.getParameter("option");
        StringBuilder result = new StringBuilder();

        try {
            String procedure = null;
            switch (option) {
                case "Get_The_Maximum_Status_Of_All_Suppliers":
                    procedure = "{CALL Get_The_Maximum_Status_Of_All_Suppliers()}";
                    break;
                case "Get_The_Name_Of_The_Job_With_The_Most_Workers":
                    procedure = "{CALL Get_The_Name_Of_The_Job_With_The_Most_Workers()}";
                    break;
                case "Get_The_Sum_Of_All_Parts_Weights":
                    procedure = "{CALL Get_The_Sum_Of_All_Parts_Weights()}";
                    break;
                case "Get_The_Total_Number_Of_Shipments":
                    procedure = "{CALL Get_The_Total_Number_Of_Shipments()}";
                    break;
                case "List_The_Name_And_Status_Of_All_Suppliers":
                    procedure = "{CALL List_The_Name_And_Status_Of_All_Suppliers()}";
                    break;
                default:
                    result.append("Invalid option selected.");
            }

            if (procedure != null) {
                CallableStatement callableStatement = connection.prepareCall(procedure);
                boolean hasResultSet = callableStatement.execute();
                if (hasResultSet) {
                    ResultSet rs = callableStatement.getResultSet();
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    // Create table HTML
                    result.append("<table>");
                    result.append("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        result.append("<th>").append(metaData.getColumnName(i)).append("</th>");
                    }
                    result.append("</tr>");

                    // Print rows
                    while (rs.next()) {
                        result.append("<tr>");
                        for (int i = 1; i <= columnCount; i++) {
                            result.append("<td>").append(rs.getString(i)).append("</td>");
                        }
                        result.append("</tr>");
                    }
                    result.append("</table>");
                } else {
                    result.append("No results found.");
                }
                callableStatement.close();
            }

        } catch (SQLException e) {
            throw new ServletException("SQL error: " + e.getMessage(), e);
        }

        request.setAttribute("queryResult", result.toString());
        request.getRequestDispatcher("/AccountantUserApp.jsp").forward(request, response);
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
