<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>View Ticket</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div style="border: 1px solid #ccc; padding: 16px;">
    <h3>Ticket #${ticket.id}: "${ticket.title}"</h3>
    <hr/>
    <p><strong>Status:</strong> ${ticket.status}</p>
    <p><strong>Priority:</strong> ${ticket.priority}</p>
    <p><strong>Category:</strong> ${ticket.category}</p>
    <p><strong>Created By:</strong> ${ticket.createdBy}</p>
    <p><strong>Assignee:</strong> ${ticket.assignee}</p>
    <p><strong>Created On:</strong> ${ticket.creationDate}</p>
    <hr/>
    <p><strong>Description:</strong><br/>
    <blockquote>${ticket.description}</blockquote></p>
    <hr/>
	<c:if test="${activeRole == 'USER'}">
	       <div class="button-group">
	           <button type="button">Close</button>
	           <button type="button">Reopen</button>
	       </div>
	   </c:if>

	   <c:if test="${activeRole == 'MANAGER'}">
	       <div class="button-group">
			<button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/approve-ticket/${ticket.id}'">Approve</button>
			<button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/reject-ticket/${ticket.id}'">Reject</button>
	       </div>
	   </c:if>

	   <c:if test="${activeRole == 'ADMIN'}">
	       <div class="button-group">
	           <button type="button">Resolve</button>
	           <button type="button">Open</button>
	           <button type="button">Close</button>
	       </div>
	   </c:if>
</div>
