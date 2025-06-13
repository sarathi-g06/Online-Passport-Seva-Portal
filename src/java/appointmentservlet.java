import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class appointmentservlet extends HttpServlet {

    // Override doPost to handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Process request method to handle both POST requests
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Fetch form data
        String fullName = request.getParameter("fullName");
        String acknowledgementNumber = request.getParameter("acknowledgementNumber");
        String contactNumber = request.getParameter("contactNumber");
        String email = request.getParameter("email");
        String appointmentDate = request.getParameter("appointmentDate");
        String appointmentTime = request.getParameter("appointmentTime");
        String appointmentLocation = request.getParameter("appointmentLocation");

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/test_ps";
        String username = "root";
        String password = "24mca0026";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PrintWriter out = response.getWriter()) {
             
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Check if the time slot is fully booked (3 appointments max)
            String countQuery = "SELECT COUNT(*) AS bookingCount FROM appointments WHERE appointmentDate = ? AND appointmentTime = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(countQuery)) {
                pstmt.setString(1, appointmentDate);
                pstmt.setString(2, appointmentTime);
                ResultSet rs = pstmt.executeQuery();
                
                rs.next();
                int bookingCount = rs.getInt("bookingCount");

                if (bookingCount >= 3) {
                    out.println("<h2>The selected time slot (" + appointmentTime + " on " + appointmentDate + ") is fully booked. Please choose another time.</h2>");
                } else {
                    // Insert the new appointment if the slot is available
                    String sql = "INSERT INTO appointments (acknowledgementNumber, fullName, contactNumber, email, appointmentDate, appointmentTime, appointmentLocation) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
                        insertStmt.setString(1, acknowledgementNumber);
                        insertStmt.setString(2, fullName);
                        insertStmt.setString(3, contactNumber);
                        insertStmt.setString(4, email);
                        insertStmt.setString(5, appointmentDate);
                        insertStmt.setString(6, appointmentTime);
                        insertStmt.setString(7, appointmentLocation);

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            //out.println("<h2>Appointment booked successfully!</h2>");
                            response.sendRedirect("bookedmsg.html");
                        } else {
                            out.println("<h2>Failed to book the appointment. Please try again.</h2>");
                            response.sendRedirect("booking.html");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database connection or SQL issue.", e);
        }
    }
}
