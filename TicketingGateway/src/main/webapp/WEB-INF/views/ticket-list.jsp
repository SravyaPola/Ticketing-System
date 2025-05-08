<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Your Tickets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div>
    <table border="1" width="100%" cellpadding="8">
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Assignee</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="ticket" items="${ticketList}">
            <tr>
                <td>${ticket.id}</td>
                <td>${ticket.title}</td>
                <td>${ticket.priority}</td>
                <td>${ticket.status}</td>
                <td><c:out value="${ticket.assignee != null ? ticket.assignee : 'N/A'}" /></td>
                <td>
                    <button type="button" onclick="window.location.href = ${pageContext.request.contextPath}/user/view-ticket/${ticket.id}>View Ticket</button>
                    <button type="button" onclick="window.location.href = ${pageContext.request.contextPath}/user/update-ticket/${ticket.id}>Update Ticket</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
