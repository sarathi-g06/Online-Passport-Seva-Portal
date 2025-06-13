import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class StoreAppIdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appId = request.getParameter("appId"); // Get the Application ID from the form
        
        // Store the appId in the session
        HttpSession session = request.getSession();
        session.setAttribute("appId", appId);
        
        // Redirect to the application status page
        response.sendRedirect("viewapplicationstatus.jsp");
    }
}
