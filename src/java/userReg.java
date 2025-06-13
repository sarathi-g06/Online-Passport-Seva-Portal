import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class userReg extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_ps";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "24mca0026";

    // JDBC variables
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            // Initialize the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new ServletException("Database connection error", e);
        }
    }

    // Handle POST request to insert data
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get input parameters from the form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        
        try {
            // Create SQL insert statement
            String sql = "INSERT INTO users (username, email, user_password) VALUES (?, ?, ?)";

            // Create a PreparedStatement to execute the query
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Set parameters for the prepared statement
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password); // Ideally, hash the password before storing

                // Execute the query
                int rowsAffected = stmt.executeUpdate();

                // Respond based on success or failure
                if (rowsAffected > 0) {
                    response.getWriter().println("User registered successfully..");
                    response.sendRedirect("login.html");
                    
                } else {
                    response.getWriter().println("User registration failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
