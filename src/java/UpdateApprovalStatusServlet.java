import passportFunctions.PassportNumberGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateApprovalStatusServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appId = request.getParameter("appId");
        String action = request.getParameter("action");
        String newStatus = action.equals("approve") ? "a" : "r";
        String passportNo = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_ps", "root", "24mca0026");

            if ("approve".equals(action)) {
                passportNo = PassportNumberGenerator.generatePassportNumber();
            }

            String query = "UPDATE passport_applications SET approvalstatus = ?, passport_no = ? WHERE app_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newStatus);
            ps.setString(2, passportNo);
            ps.setString(3, appId);

            int rowsUpdated = ps.executeUpdate();
            conn.close();

            response.setContentType("application/json");
            if (rowsUpdated > 0) {
                response.getWriter().write("{\"status\":\"success\", \"appId\":\"" + appId + "\", \"newStatus\":\"" + newStatus + "\", \"passportNo\":\"" + passportNo + "\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Failed to update the status\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error: " + e.getMessage() + "\"}");
        }
    }
}
