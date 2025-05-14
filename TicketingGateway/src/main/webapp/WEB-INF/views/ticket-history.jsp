<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
    <title>Ticket History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div>
    <h2>Ticket History</h2>
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
				<td><fmt:formatDate value="${entry.actionDateAsDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>${entry.comments}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
