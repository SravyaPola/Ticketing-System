<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
	<head>
		<c:if test="${activeRole == 'MANAGER'}">
		    <title>Tickets to Approve</title>
		</c:if>
		<c:if test="${activeRole == 'USER'}">
		    <title>Your Tickets</title>
		</c:if>
	</head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div>
	<c:if test="${activeRole == 'MANAGER'}">
	    <h2>Tickets to Approve</h2>
	</c:if>
	<c:if test="${activeRole == 'USER'}">
	    <h2>Your Tickets</h2>
	</c:if>

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
	    <c:forEach var="ticket" items="${ticketList}" varStatus="status">
	        <tr>
	            <td>${status.index + 1}</td>
	            <td>${ticket.title}</td>
	            <td>${ticket.priority}</td>
	            <td>${ticket.status}</td>
	            <td><c:out value="${ticket.assignee != null ? ticket.assignee : 'N/A'}" /></td>
	            <td class="ticket-actions">
	                <div class="action-buttons">
						<c:if test="${activeRole == 'USER'}">
						    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/view-ticket/${ticket.id}'">View</button>
						</c:if>
						<c:if test="${activeRole == 'MANAGER'}">
						    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/view-ticket/${ticket.id}'">View</button>
						</c:if>
						<c:if test="${activeRole == 'USER'}">
						    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/update-ticket/${ticket.id}'">Update</button>
						</c:if>
						<c:if test="${activeRole == 'USER'}">
						    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/ticket-history/${ticket.id}'">History</button>
						</c:if>
						<c:if test="${activeRole == 'MANAGER'}">
						    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/ticket-history/${ticket.id}'">History</button>
						</c:if>
	                </div>
	            </td>
	        </tr>
	    </c:forEach>
	    </tbody>
	</table>
</div>
