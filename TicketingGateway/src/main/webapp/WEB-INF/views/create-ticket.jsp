<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Ticket</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
	
	<h2>Create New Ticket</h2>
    <form action="${pageContext.request.contextPath}/submitTicket" method="post" enctype="multipart/form-data">
		<c:if test="${not empty message}">
		    <div class="message" style="text-align: center; font-weight: bold; color: green;">
		        ${message}
		    </div>
		</c:if>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required>

        <label for="description">Description:</label>
        <textarea id="description" name="description" required></textarea>

        <label for="priority">Priority:</label>
        <select id="priority" name="priority" required>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
        </select>

        <label for="category">Category:</label>
        <select id="category" name="category" required>
            <option value="BUG">BUG</option>
            <option value="FEATURE_REQUEST">FEATURE_REQUEST</option>
            <option value="TASK">TASK</option>
            <option value="IMPROVEMENT">IMPROVEMENT</option>
            <option value="DOCUMENTATION">DOCUMENTATION</option>
            <option value="MAINTENANCE">MAINTENANCE</option>
            <option value="SUPPORT">SUPPORT</option>
        </select>

        <label for="files">File Attachments:</label>
        <input type="file" id="files" name="files" multiple />

        <div class="form-buttons">
            <button type="submit">Submit Ticket</button>
            <button type="reset">Cancel</button>
        </div>
		<a href="/user/home">Back To Dashboard</a>
    </form>
</div>

</body>
</html>
