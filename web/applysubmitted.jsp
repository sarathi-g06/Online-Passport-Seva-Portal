<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Appointment Confirmation</title>
    <link rel="stylesheet" href="./css/bookedmsg.css">
</head>
<body>
    <%
        String applicationId = request.getParameter("appid");
    
    %>

    <div class="confirmation-container">
        <h2>Application Status</h2>
        <p>Your application has been successfully submitted!</p>

        <!-- Display Application ID -->
        <p><strong>Application ID:</strong> <%= applicationId %></p>

        <button class="home-btn" onclick="window.location.href='index.html'">Back to Home</button>
    </div>

</body>
</html>
