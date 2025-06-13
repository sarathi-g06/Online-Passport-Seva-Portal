import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class userLogin extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_ps";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "24mca0026";

    // JDBC connection
    private Connection conn;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new ServletException("Database connection error", e);
        }
    }

    // Handle POST request for login authentication
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get input parameters from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        try {
            // If admin credentials are provided, redirect to the Admin page
            if ("admin".equals(username) && "admin123".equals(password)) {
                response.sendRedirect("Admin.html");
                return;
            }

            // SQL query to check user credentials
            String sql = "SELECT * FROM users WHERE username = ? AND user_password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Password should ideally be hashed

                ResultSet rs = stmt.executeQuery();

                // If user credentials are valid, redirect to application page
                if (rs.next()) {
                    response.sendRedirect("apply.html");
                } else {
                    // Redirect back to login with an error message
                    response.sendRedirect("login.html?error=invalid");
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
