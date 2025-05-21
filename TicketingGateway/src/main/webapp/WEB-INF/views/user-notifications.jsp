<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Your Notifications</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <style>
    .notifications { list-style: none; padding: 0; }
    .notification {
      border: 1px solid #ccc;
      border-radius: 4px;
      padding: 12px;
      margin: 8px 0;
      background: #f9f9f9;
    }
    .notification h3 {
      margin: 0 0 4px;
      font-size: 1.1em;
    }
    .notification p {
      margin: 2px 0;
    }
    .back-link {
      display: inline-block;
      margin-top: 16px;
    }
  </style>
</head>
<body>
  <h1>Your Notifications</h1>

  <c:choose>
      <c:when test="${empty notifications}">
        <p>No notifications to display.</p>
      </c:when>
      <c:otherwise>
        <ul style="list-style:none; padding:0;">
          <c:forEach var="n" items="${notifications}">
            <li style="border:1px solid #ccc; padding:8px; margin:4px 0;">
              <p><strong>Employee:</strong> <c:out value="${n.employeeId}"/></p>
              <p><strong>Message:</strong>  <c:out value="${n.message}"/></p>
              <p><strong>Status:</strong>   <c:out value="${n.status}"/></p>
            </li>
          </c:forEach>
        </ul>
      </c:otherwise>
    </c:choose>

  <a class="back-link" href="<c:url value='/user/home'/>">← Back to Dashboard</a>
</body>
</html>
