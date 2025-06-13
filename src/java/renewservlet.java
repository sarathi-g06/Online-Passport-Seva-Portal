/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class renewservlet extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_ps";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "24mca0026";

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

    // Handle POST request to store passport renewal details in the database
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String passportNumber = request.getParameter("passportNumber");
        String issueDate = request.getParameter("issueDate");
        String expiryDate = request.getParameter("expiryDate");
        String placeOfIssue = request.getParameter("placeOfIssue");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Prepare SQL query to insert data into the database
        String sql = "INSERT INTO passport_renewals (passportNumber, issueDate, expiryDate, placeOfIssue, email, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set parameters for the PreparedStatement
            stmt.setString(1, passportNumber);
            stmt.setString(2, issueDate);
            stmt.setString(3, expiryDate);
            stmt.setString(4, placeOfIssue);
            stmt.setString(5, email);
            stmt.setString(6, phone);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Respond based on success or failure
            if (rowsAffected > 0) {
                response.getWriter().println("Passport renewal application submitted successfully.");
                response.sendRedirect("renewalmsg.html");
                
            } else {
                response.getWriter().println("Failed to submit the renewal application.");
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

