<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Create Ticket</title>
  <link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/ticket.css"
  />
</head>
<body>
  <div class="container">
    <h2>Create New Ticket</h2>

    <c:if test="${not empty message}">
      <div class="message success">
        ${message}
      </div>
    </c:if>

    <form
      action="${pageContext.request.contextPath}/user/submit-ticket"
      method="post"
      enctype="multipart/form-data"
    >
      <!-- Title (required) -->
      <label for="title">Title:</label>
      <input
        type="text"
        id="title"
        name="title"
        required
      />

      <!-- Description (required) -->
      <label for="description">Description:</label>
      <textarea
        id="description"
        name="description"
        required
      ></textarea>

      <!-- Priority (required) -->
      <label for="priority">Priority:</label>
      <select
        id="priority"
        name="priority"
        required
      >
        <option value="">-- Select Priority --</option>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
      </select>

      <!-- Category (required) -->
      <label for="category">Category:</label>
      <select
        id="category"
        name="category"
        required
      >
        <option value="">-- Select Category --</option>
        <option value="BUG">BUG</option>
        <option value="FEATURE_REQUEST">FEATURE_REQUEST</option>
        <option value="TASK">TASK</option>
        <option value="IMPROVEMENT">IMPROVEMENT</option>
        <option value="DOCUMENTATION">DOCUMENTATION</option>
        <option value="MAINTENANCE">MAINTENANCE</option>
        <option value="SUPPORT">SUPPORT</option>
      </select>

      <!-- File Attachments (required) -->
      <label for="files">File Attachments:</label>
      <input
        type="file"
        id="files"
        name="files"
        multiple
        required
      />

      <!-- Submit / Cancel -->
      <div class="form-buttons">
        <button type="submit">Submit Ticket</button>
        <button type="reset">Cancel</button>
      </div>

      <!-- Back link -->
      <a href="${pageContext.request.contextPath}/user/home">
        ← Back To Dashboard
      </a>
    </form>
  </div>
</body>
</html>
