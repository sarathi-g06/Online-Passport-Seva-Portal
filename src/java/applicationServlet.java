
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Random;

@MultipartConfig
public class applicationServlet extends HttpServlet {

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

    // Handle POST request to retrieve form data and insert into the database
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String birthPlace = request.getParameter("birthPlace");
        String nationality = request.getParameter("nationality");
        String maritalStatus = request.getParameter("maritalStatus");
        String fatherName = request.getParameter("fatherName");
        String motherName = request.getParameter("motherName");
        String permanentAddress = request.getParameter("permanentAddress");
        String correspondenceAddress = request.getParameter("correspondenceAddress");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String aadharNumber = request.getParameter("aadharNumber");
        String voterID = request.getParameter("voterID");
        String panCard = request.getParameter("panCard");

        // Handling the file upload (proof of identity)
        Part identityProofPart = request.getPart("identityProof");
        if (identityProofPart == null || identityProofPart.getSize() == 0) {
            response.getWriter().println("No file uploaded for identity proof.");
            return;
        }

        String identityProofFilename = identityProofPart.getSubmittedFileName();
        String identityProofFilePath = "./upload" + identityProofFilename; // Update the path

        // Save the uploaded file
        identityProofPart.write(identityProofFilePath);

        // Generate a random 12-character alphanumeric passport reference number
        String passportRefNumber = generatePassportRefNumber();
        
        String policeVerificationStatus = generatePoliceVerificationStatus();


        // Prepare SQL query to insert the data into the database
        String sql = "INSERT INTO passport_applications (app_id, fullName, gender, dob, birthPlace, nationality, maritalStatus, "
                + "fatherName, motherName, permanentAddress, correspondenceAddress, phone, email, aadharNumber, "
                + "voterID, panCard, identityProof, police_verification) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the parameters for the PreparedStatement
            stmt.setString(1, passportRefNumber); // Insert the generated passport reference number (app_id)
            stmt.setString(2, fullName);
            stmt.setString(3, gender);
            stmt.setString(4, dob);
            stmt.setString(5, birthPlace);
            stmt.setString(6, nationality);
            stmt.setString(7, maritalStatus);
            stmt.setString(8, fatherName);
            stmt.setString(9, motherName);
            stmt.setString(10, permanentAddress);
            stmt.setString(11, correspondenceAddress);
            stmt.setString(12, phone);
            stmt.setString(13, email);
            stmt.setString(14, aadharNumber);
            stmt.setString(15, voterID);
            stmt.setString(16, panCard);
            stmt.setString(17, identityProofFilePath); // Save the path to the file
            stmt.setString(18, policeVerificationStatus);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Respond based on success or failure
            if (rowsAffected > 0) {
                String appid2 = passportRefNumber;
                //response.getWriter().println("Application submitted successfully. Your Passport Reference Number is: " + passportRefNumber);
                //response.sendRedirect("appsubmitted.html"+appid2);
                response.sendRedirect("applysubmitted.jsp?appid=" + passportRefNumber);
                //response.sAttribute();
            } else {
                response.getWriter().println("Failed to submit the application.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Database error: " + e.getMessage());
        }

    }

    // Helper method to generate a random 12-character alphanumeric string
    private String generatePassportRefNumber() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder refNumber = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(characters.length());
            refNumber.append(characters.charAt(randomIndex));
        }

        return refNumber.toString();
    }

    private String generatePoliceVerificationStatus() {
        Random random = new Random();
        return random.nextInt(2) == 0 ? "completed" : "pending";
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
