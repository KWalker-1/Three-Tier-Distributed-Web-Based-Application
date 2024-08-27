import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

public class rootExecuteQueryServlet extends HttpServlet {
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Properties properties = new Properties();
            FileInputStream filein = new FileInputStream("/Library/Tomcat10125/webapps/Project-3/WEB-INF/lib/root.properties");
            properties.load(filein);
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
        } catch (Exception e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sqlQuery = request.getParameter("sqlQuery");
        StringBuilder result = new StringBuilder();

        try {
            Statement statement = connection.createStatement();
            boolean hasResultSet = statement.execute(sqlQuery);

            if (hasResultSet) {
                ResultSet rs = statement.getResultSet();
                result.append(processResultSet(rs));
            } else {
                int updateCount = statement.getUpdateCount();
                result.append("Query executed successfully. Rows affected: ").append(updateCount);

                // Check if the query affected the `shipments` table
                if (sqlQuery.trim().toUpperCase().startsWith("INSERT INTO SHIPMENTS") || 
                    sqlQuery.trim().toUpperCase().startsWith("UPDATE SHIPMENTS")) {
                    triggerBusinessLogic(statement, result);
                }
            }
        } catch (SQLException e) {
            result.append("SQL Error: ").append(e.getMessage());
        }

        request.setAttribute("queryResult", result.toString());
        request.getRequestDispatcher("rootHome.jsp").forward(request, response);
    }

    private String processResultSet(ResultSet rs) throws SQLException {
        StringBuilder result = new StringBuilder();
        result.append("<table>");
        int columnCount = rs.getMetaData().getColumnCount();
        result.append("<tr>");
        for (int i = 1; i <= columnCount; i++) {
            result.append("<th>").append(rs.getMetaData().getColumnName(i)).append("</th>");
        }
        result.append("</tr>");
        while (rs.next()) {
            result.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                result.append("<td>").append(rs.getString(i)).append("</td>");
            }
            result.append("</tr>");
        }
        result.append("</table>");
        return result.toString();
    }

    private void triggerBusinessLogic(Statement statement, StringBuilder result) throws SQLException {
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
