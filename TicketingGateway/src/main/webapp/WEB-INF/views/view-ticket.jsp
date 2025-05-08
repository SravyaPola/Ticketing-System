<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Assume ticket is fetched via servlet/controller and added to request scope as "ticket"
%>
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
    <p><strong>Created By:</strong> ${ticket.createdBy} (Dept: ${ticket.createdByDepartment})</p>
    <p><strong>Assignee:</strong> ${ticket.assignee}</p>
    <p><strong>Created On:</strong> ${ticket.creationDate}</p>
    <p><strong>Attachment:</strong>
        <c:if test="${not empty ticket.attachmentPath}">
            [${ticket.attachmentPath}] <a href="download?file=${ticket.attachmentPath}">Download</a>
        </c:if>
        <c:if test="${empty ticket.attachmentPath}">
            None
        </c:if>
    </p>
    <hr/>
    <p><strong>Description:</strong><br/>
    <blockquote>${ticket.description}</blockquote></p>
    <hr/>
    <div>
        <c:if test="${role == 'MANAGER'}">
            <button>Approve</button>
            <button>Reject</button>
        </c:if>
        <c:if test="${role == 'ADMIN'}">
            <button>Resolve</button>
            <button>Close</button>
            <button>Reopen</button>
        </c:if>
		<c:if test="${role == 'USER'}">
		    <button>Close</button>
		    <button>Reopen</button>
		</c:if>
    </div>
</div>
