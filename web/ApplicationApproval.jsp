<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="mypackage.Application" %>
<%@ page import="passportFunctions.PassportNumberGenerator" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Application Approval</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { width: 80%; margin: auto; padding: 20px; text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid black; }
        th, td { padding: 10px; text-align: center; }
        th { background-color: #f2f2f2; }
        button { padding: 5px 10px; font-size: 14px; cursor: pointer; }
        button:disabled { background-color: #ccc; cursor: not-allowed; }
    </style>
    <script>
        function updateStatus(appId, action) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "UpdateApprovalStatusServlet", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var response = JSON.parse(xhr.responseText);
                    if (response.status == "success") {
                        var row = document.getElementById("row-" + appId);
                        var statusCell = row.querySelector(".status");
                        var passportCell = row.querySelector(".passportNo");
                        var actionCell = row.querySelector(".action");

                        statusCell.innerText = (response.newStatus == "a") ? "Approved" : "Rejected";
                        if (response.newStatus == "a" && response.passportNo) {
                            passportCell.innerText = response.passportNo;
                        }

                        var approveButton = actionCell.querySelector(".approve");
                        var rejectButton = actionCell.querySelector(".reject");

                        approveButton.disabled = true;
                        rejectButton.disabled = true;

                        row.style.backgroundColor = (response.newStatus == "a") ? "#d4edda" : "#f8d7da";
                    } else {
                        alert("Error: " + response.message);
                    }
                }
            };
            xhr.send("appId=" + appId + "&action=" + action);
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Application Approval</h1>
        <table>
            <thead>
                <tr>
                    <th>Application ID</th>
                    <th>Full Name</th>
                    <th>Date of Birth</th>
                    <th>Nationality</th>
                    <th>Police Verification</th>
                    <th>Approval Status</th>
                    <th>Passport No</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    ArrayList<Application> applications = (ArrayList<Application>) request.getAttribute("applications");
                    if (applications != null) {
                        for (Application app : applications) {
                %>
                    <tr id="row-<%= app.getAppId() %>">
                        <td><%= app.getAppId() %></td>
                        <td><%= app.getFullName() %></td>
                        <td><%= app.getDob() %></td>
                        <td><%= app.getNationality() %></td>
                        <td><%= app.getPoliceVerification() %></td>
                        <td class="status"><%= app.getApprovalStatus().equals("inprocess") ? "In Process" : (app.getApprovalStatus().equals("a") ? "Approved" : "Rejected") %></td>
                        <td class="passportNo"><%= app.getPassportNo() != null ? app.getPassportNo() : "" %></td>
                        <td class="action">
                            <button class="approve" onclick="updateStatus('<%= app.getAppId() %>', 'approve')" <%= "completed".equals(app.getPoliceVerification()) ? "" : "disabled" %>>Approve</button>
                            <button class="reject" onclick="updateStatus('<%= app.getAppId() %>', 'reject')">Reject</button>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="8">No applications found.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
