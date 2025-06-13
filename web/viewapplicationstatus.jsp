<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.ResultSet" %>
<%@ page import="mypackage.Application" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Application Status</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
        }
        .container {
            width: 50%;
            margin: auto;
            padding: 20px;
            text-align: center;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .status {
            font-weight: bold;
        }
        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Application Status</h1>

        <%
            String appId = request.getParameter("appId");

            if (appId != null && !appId.isEmpty()) {
                String DB_URL = "jdbc:mysql://localhost:3306/test_ps";
                String DB_USER = "root";
                String DB_PASSWORD = "24mca0026";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                         PreparedStatement ps = conn.prepareStatement(
                             "SELECT app_id, fullName, police_verification, approvalstatus, passport_no FROM passport_applications WHERE app_id = ?")) {

                        ps.setString(1, appId);
                        ResultSet rs = ps.executeQuery();

                        Application app = null;
                        if (rs.next()) {
                            app = new Application();
                            app.setAppId(rs.getString("app_id"));
                            app.setFullName(rs.getString("fullName"));
                            app.setPoliceVerification(rs.getString("police_verification"));
                            app.setApprovalStatus(rs.getString("approvalstatus"));
                            app.setPassportNo(rs.getString("passport_no"));
                        }

                        if (app != null) {
        %>

        <table>
            <tr>
                <th>Application ID</th>
                <th>Full Name</th>
                <th>Police Verification Status</th>
                <th>Approval Status</th>
                <th>Passport Number</th>
            </tr>
            <tr>
                <td><%= app.getAppId() %></td>
                <td><%= app.getFullName() %></td>
                <td><%= app.getPoliceVerification() %></td>
                <td class="status">
                    <%= "a".equals(app.getApprovalStatus()) ? "Approved" : 
                        ("r".equals(app.getApprovalStatus()) ? "Rejected" : "In Process") %>
                </td>
                <td class="status">
                    <%= app.getPassportNo() != null ? app.getPassportNo() : "Process Ongoing" %>
                </td>
            </tr>
        </table>

        <%
                        } else {
        %>
                    <p>No application found with the provided Application ID.</p>
        <%
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
        %>
                    <p class="error">An error occurred while retrieving the application status. Please try again later.</p>
        <%
                }
            } else {
        %>
            <p class="error">Please log in and provide a valid Application ID to view the status.</p>
        <%
            }
        %>

    </div>
</body>
</html>
