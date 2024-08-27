import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

@WebServlet("/ExecuteQueryServlet")
public class ExecuteQueryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            Properties properties = new Properties();
            FileInputStream filein = new FileInputStream("/Library/Tomcat10125/webapps/Project-3/WEB-INF/lib/client.properties");
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

        try (Statement statement = connection.createStatement()) {
            boolean hasResultSet = statement.execute(sqlQuery);
            if (hasResultSet) {
                ResultSet rs = statement.getResultSet();
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
                int updateCount = statement.getUpdateCount();
                result.append("Query executed successfully. Rows affected: ").append(updateCount);
            }
        } catch (SQLException e) {
            result.append("SQL Error: ").append(e.getMessage());
        }

        request.setAttribute("queryResult", result.toString());
        request.getRequestDispatcher("ClientUserApp.jsp").forward(request, response);
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
