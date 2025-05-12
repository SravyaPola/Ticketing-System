<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Update Ticket</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div style="border: 1px solid #ccc; padding: 16px;">
	
	<c:if test="${not empty message}">
	    <div style="text-align: center; color: green; font-weight: bold; margin-bottom: 10px;">
	        ${message}
	    </div>
	</c:if>
    <h3>Update Ticket ${ticket.id}: "${ticket.title}"</h3>
    <hr/>

    <form action="${pageContext.request.contextPath}/user/update-ticket/${ticket.id}"
          method="post"
          enctype="multipart/form-data"
          class="ticket-form">

        <input type="hidden" name="id" value="${ticket.id}"/>

        <p><strong>Status:</strong> ${ticket.status} </p>

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

        <p><strong>Created By:</strong> ${ticket.createdBy}</p>
        <p><strong>Assignee:</strong> ${ticket.assignee}</p>
        <p><strong>Created On:</strong> ${ticket.creationDate}</p>

        <p><strong>Add New Attachments:</strong><br/>
            <input type="file" id="files" name="files" multiple />
        </p>

        <hr/>
        <p><strong>Description:</strong><br/>
            <textarea name="description" required rows="5" cols="60">${ticket.description}</textarea>
        </p>

        <div class="button-group">
            <button type="submit">Save Changes</button>
            <button type="button" onclick="window.history.back()">Cancel</button>
        </div>
    </form>
</div>

</body>
</html>
