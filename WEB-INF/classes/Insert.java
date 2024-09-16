import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/insert")
public class Insert extends HttpServlet {
  public static Connection connect() {
        // JDBC URL, username et password de la base de données
        String url = "jdbc:postgresql://localhost:5432/tomcat_db"; // Remplacez avec votre nom de base de données
        String user = "tomcat_user"; // Remplacez avec votre nom d'utilisateur
        String password = "tomcat_psw"; // Remplacez avec votre mot de passe

        Connection connection = null;

        try {
            // Créer la connexion à la base de données
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie à la base de données PostgreSQL");

        } catch (SQLException e) {
            System.out.println("Échec de la connexion à la base de données");
            e.printStackTrace();
        }

        return connection;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // create form with name, pseudo
        response.getWriter().println("<form method='post'>");
        response.getWriter().println("Name: <input type='text' name='name'><br>");
        response.getWriter().println("Pseudo: <input type='text' name='pseudo'><br>");
        response.getWriter().println("<input type='submit' value='Submit'>");
        response.getWriter().println("</form>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Secure SQL query
        String query = "INSERT INTO your_table (name, pseudo) VALUES (?, ?)"; // Update with your table name
        
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the connection
            connection = connect();

            // Create a statement
            stmt = connection.createStatement();

            // Execute the query
            // rs = stmt.executeQuery(query);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, request.getParameter("name"));
            ps.setString(2, request.getParameter("pseudo"));
            ps.executeUpdate();

            // get column names
            // for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            //     response.getWriter().println(rs.getMetaData().getColumnName(i) + " ");
            // }

            // Process the ResultSet
            // while (rs.next()) {
            //     response.getWriter().println("<br>");
            //     for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            //         response.getWriter().println(rs.getString(i) + " ");
            //     }
            // }

        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        } finally {
            // Close the ResultSet, Statement, and Connection in reverse order
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // redirect to the same page
        response.sendRedirect("/tp501/select");

        /**
         * CURL example: curl -X POST -d "name=John&pseudo=doe" http://localhost:8080/tp501/insert
         */
    }
}
