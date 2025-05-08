<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <title>Ticket History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div>
    <h3>Ticket History</h3>
    <table border="1" width="100%" cellpadding="8">
        <thead>
            <tr>
                <th>#</th>
                <th>Action</th>
                <th>Action By</th>
                <th>Action Date</th>
                <th>Comments</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="entry" items="${ticketHistoryList}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${entry.action}</td>
                <td>${entry.actionBy} (${entry.role})</td>
                <td>${entry.actionDate}</td>
                <td>${entry.comments}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
