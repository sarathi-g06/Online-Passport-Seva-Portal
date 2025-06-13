import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mypackage.Application;

public class ApplicationApprovalServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Application> applications = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_ps", "root", "24mca0026");

            String query = "SELECT app_id, fullName, dob, nationality, police_verification, approvalstatus, passport_no FROM passport_applications WHERE approvalstatus = 'inprocess'";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Application application = new Application();
                application.setAppId(rs.getString("app_id"));
                application.setFullName(rs.getString("fullName"));
                application.setDob(rs.getDate("dob"));
                application.setNationality(rs.getString("nationality"));
                application.setPoliceVerification(rs.getString("police_verification"));
                application.setApprovalStatus(rs.getString("approvalstatus"));
                application.setPassportNo(rs.getString("passport_no"));
                applications.add(application);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("applications", applications);
        request.getRequestDispatcher("ApplicationApproval.jsp").forward(request, response);
    }
}
