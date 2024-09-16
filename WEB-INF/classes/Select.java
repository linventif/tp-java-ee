import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/select")
public class Select extends HttpServlet {

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

        // add cool css and glow glod effect on hover dark mode
        response.getWriter().println(
            "<style>body {background-color: #333333; color: white; font-family: Arial, sans-serif;}</style>");
        response.getWriter().println(
            "<style>h1 {color: #FFD700; text-align: center; font-size: 50px; text-shadow: 2px 2px 4px #000000;}</style>");
        response.getWriter().println(
            "<style>table {width: 100%; border-collapse: collapse;} th, td {padding: 8px; text-align: left; border-bottom: 1px solid #ddd;} tr:hover {background-color: #525252;}</style>");
        response.getWriter().println(
            "<style>th {background-color: #FFD700; color: black;}</style>");
        response.getWriter().println(
            "<style>td {color: white;}</style>");
        response.getWriter().println("<h1>Table content</h1>");


        // SQL query
        String query = "SELECT * FROM your_table"; // Update with your table name

        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the connection
            connection = connect();

            // Create a statement
            stmt = connection.createStatement();

            // Execute the query
            rs = stmt.executeQuery(query);

            // Create HTML table
            response.getWriter().println("<table>");
            
            // Create table header
            response.getWriter().println("<tr>");
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
          response.getWriter().println("<th>" + rs.getMetaData().getColumnName(i) + "</th>");
            }
            response.getWriter().println("</tr>");

            // Process the ResultSet
            while (rs.next()) {
          response.getWriter().println("<tr>");
          for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
              response.getWriter().println("<td>" + rs.getString(i) + "</td>");
          }
          response.getWriter().println("</tr>");
            }

            // Close the HTML table
            response.getWriter().println("</table>");

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

        response.getWriter().println("<h1>Insert</h1>");

          // create form with name, pseudo
        response.getWriter().println("<form method='post' action='/tp501/insert' target='_self'>"); // Modified line
        response.getWriter().println("Name: <input type='text' name='name'><br>");
        response.getWriter().println("Pseudo: <input type='text' name='pseudo'><br>");
        response.getWriter().println("<input type='submit' value='Submit'>");
        response.getWriter().println("</form>");


        /**
         * CURL example: curl -X GET http://localhost:8080/tp501/select
         */
    }
}
