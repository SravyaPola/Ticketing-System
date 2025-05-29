<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Update Ticket</title>
    <!-- Inlined “card” styles with tighter spacing and a new vibrant palette -->
    <style>
      /* Base page styles */
      body {
        margin: 0;
        padding: 20px;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #f0f4f8;
        color: #333;
      }

      /* Success message */
      .success-message {
        background-color: #e8f5e9;
        border: 1px solid #c8e6c9;
        color: #2e7d32;
        border-radius: 4px;
        padding: 12px;
        margin-bottom: 20px;
        text-align: center;
        font-weight: 600;
      }

      /* Form “card” container */
      .ticket-card {
        max-width: 800px;
        margin: 0 auto;
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        padding: 30px;
      }

      /* Heading */
      .ticket-card h3 {
        margin-top: 0;
        margin-bottom: 20px;
        font-size: 1.75rem;
        color: #8e44ad; /* vibrant purple */
      }

      /* Tighter spacing between fields */
      .ticket-card p {
        margin-bottom: 12px;
      }
      .ticket-card strong {
        display: block;
        margin-bottom: 4px;
        font-weight: 600;
        color: #555;
      }

      /* Inputs */
      .ticket-card select,
      .ticket-card textarea,
      .ticket-card input[type="file"] {
        width: 100%;
        padding: 10px 12px;
        border: 1px solid #ccc;
        border-radius: 4px;
        font-size: 1rem;
        transition: border-color 0.3s ease;
      }
      .ticket-card select:focus,
      .ticket-card textarea:focus {
        outline: none;
        border-color: #8e44ad; /* purple focus */
      }
      .ticket-card textarea {
        min-height: 140px;
        resize: vertical;
      }

      /* Button group tweaks */
      .button-group {
        display: flex;
        justify-content: flex-end;
        gap: 8px;
        margin-top: 20px;
      }
      .button-group button {
        padding: 12px 24px;
        font-size: 1rem;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        transition: background-color 0.3s ease;
      }

      /* Save (purple) */
      .button-group button[type="submit"] {
        background-color: #8e44ad;
        color: #fff;
      }
      .button-group button[type="submit"]:hover {
        background-color: #732d91;
      }

      /* Cancel (orange) */
      .button-group button[type="button"] {
        background-color: #f39c12;
        color: #fff;
      }
      .button-group button[type="button"]:hover {
        background-color: #d68910;
      }
    </style>
</head>
<body>

<div class="ticket-card">
    <c:if test="${not empty message}">
        <div class="success-message">
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
