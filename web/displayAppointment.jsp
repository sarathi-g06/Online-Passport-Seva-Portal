<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Appointment Details</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #4CAF50;
            color: white;
            padding: 20px;
            text-align: center;
        }
        h2 {
            color: #333;
        }
        .container {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f8f8;
            color: #333;
        }
        td {
            color: #555;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .footer {
            text-align: center;
            padding: 20px;
            background-color: #4CAF50;
            color: white;
            position: fixed;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>

<header>
    <h1>Appointment Management System</h1>
</header>

<div class="container">
    <h2>Appointment Details</h2>

    <table>
        <tr>
            <th>Full Name</th>
            <th>Application Number</th>
            <th>Date</th>
            <th>Time</th>
            <th>Location</th>
        </tr>
        
        <%
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/test_ps";
            String username = "root";
            String password = "24mca0026";

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection
                conn = DriverManager.getConnection(url, username, password);

                // Execute SQL query
                String sql = "SELECT fullName, acknowledgementNumber, appointmentDate, appointmentTime, appointmentLocation FROM appointments";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                // Loop through the result set and display each record in a table row
                while (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String acknowledgementNumber = rs.getString("acknowledgementNumber");
                    Date appointmentDate = rs.getDate("appointmentDate");
                    String appointmentTime = rs.getString("appointmentTime");
                    String appointmentLocation = rs.getString("appointmentLocation");
        %>
                    <tr>
                        <td><%= fullName %></td>
                        <td><%= acknowledgementNumber %></td>
                        <td><%= appointmentDate %></td>
                        <td><%= appointmentTime %></td>
                        <td><%= appointmentLocation %></td>
                    </tr>
        <%
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Close resources
                if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
                if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        %>

    </table>
</div>

<div class="footer">
    <p>&copy; 2024 Appointment Management System | All Rights Reserved</p>
</div>

</body>
</html>
