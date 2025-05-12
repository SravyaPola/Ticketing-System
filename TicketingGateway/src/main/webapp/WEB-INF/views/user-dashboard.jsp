<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>User Dashboard</h2>
        <p>Hi, ${name}. You are logged in as user.</p>

        <div class="form-buttons" style="margin-top: 20px;">
            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/create-ticket'">Create Ticket</button>
            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/ticket-list'">View Tickets</button>
        </div>

        <form action="<c:url value='/logout-success' />" method="post" style="margin-top: 20px;">
            <button type="submit">Logout</button>
        </form>
    </div>
</body>
</html>
