<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Update Ticket</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <h2>Update Ticket</h2>

    <form action="${pageContext.request.contextPath}/user/update-ticket" method="post" class="ticket-form">
        <input type="hidden" name="ticketId" value="${ticket.id}"/>

        <div style="border: 1px solid #ccc; padding: 16px;">
            <h3>Ticket #${ticket.id}: "${ticket.title}"</h3>
            <hr/>

            <p><strong>Status:</strong> ${ticket.status}</p>

            <p><strong>Priority:</strong><br/>
                <select name="priority" required>
                    <option value="LOW" ${ticket.priority == 'LOW' ? 'selected' : ''}>LOW</option>
                    <option value="MEDIUM" ${ticket.priority == 'MEDIUM' ? 'selected' : ''}>MEDIUM</option>
                    <option value="HIGH" ${ticket.priority == 'HIGH' ? 'selected' : ''}>HIGH</option>
                </select>
            </p>

            <p><strong>Category:</strong><br/>
                <select name="category" required>
                    <option value="BUG" ${ticket.category == 'BUG' ? 'selected' : ''}>BUG</option>
                    <option value="FEATURE_REQUEST" ${ticket.category == 'FEATURE_REQUEST' ? 'selected' : ''}>FEATURE_REQUEST</option>
                    <option value="TASK" ${ticket.category == 'TASK' ? 'selected' : ''}>TASK</option>
                    <option value="IMPROVEMENT" ${ticket.category == 'IMPROVEMENT' ? 'selected' : ''}>IMPROVEMENT</option>
                    <option value="DOCUMENTATION" ${ticket.category == 'DOCUMENTATION' ? 'selected' : ''}>DOCUMENTATION</option>
                    <option value="MAINTENANCE" ${ticket.category == 'MAINTENANCE' ? 'selected' : ''}>MAINTENANCE</option>
                    <option value="SUPPORT" ${ticket.category == 'SUPPORT' ? 'selected' : ''}>SUPPORT</option>
                </select>
            </p>

            <p><strong>Created By:</strong> ${ticket.createdBy} (Dept: ${ticket.createdByDepartment})</p>
            <p><strong>Assignee:</strong> ${ticket.assignee}</p>
            <p><strong>Created On:</strong> ${ticket.creationDate}</p>

            <p><strong>Attachment:</strong>
                <c:if test="${not empty ticket.attachmentPath}">
                    [${ticket.attachmentPath}] <a href="${pageContext.request.contextPath}/download?file=${ticket.attachmentPath}">Download</a>
                </c:if>
                <c:if test="${empty ticket.attachmentPath}">
                    None
                </c:if>
            </p>

            <hr/>
            <p><strong>Description:</strong><br/>
                <textarea name="description" required>${ticket.description}</textarea>
            </p>

            <div class="form-buttons">
                <button type="submit">Save Changes</button>
                <button type="button" onclick="window.history.back()">Cancel</button>
            </div>
        </div>
    </form>
</div>

</body>
</html>
