<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin Notifications</title>
  <!-- Bootstrap CSS (via CDN or local copy) -->
  <link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
    rel="stylesheet"
  />
  <!-- Your common stylesheet -->
  <link
    href="${pageContext.request.contextPath}/css/notification.css"
    rel="stylesheet"
  />
</head>
<body>
  <div class="container py-4">
    <div class="row justify-content-center">
      <div class="notifications-col">

        <!-- Page title -->
        <h1 class="page-title">Admin Notifications</h1>

        <c:choose>
          <c:when test="${empty notifications}">
            <p class="text-center">No notifications to display.</p>
          </c:when>
          <c:otherwise>
            <!-- switch from UL/LI to simple DIVs -->
            <div class="notifications-list">
              <c:forEach var="n" items="${notifications}">
                <div class="notification">
                  <div><strong>Time:</strong> <c:out value="${n.receivedAt}"/></div>
                  <div><strong>Message:</strong> <c:out value="${n.message}"/></div>
                </div>
              </c:forEach>
            </div>
          </c:otherwise>
        </c:choose>

        <!-- Buttons row -->
        <div class="row row-cols-1 row-cols-sm-2 g-3 mt-4 justify-content-center">
          <div class="col notifications-btn-col">
            <button
              type="button"
              class="btn btn-back"
              onclick="location.href='${pageContext.request.contextPath}/admin/home'"
            >
              Back to Dashboard
            </button>
          </div>
          <div class="col notifications-btn-col">
            <form
              action="<c:url value='/logout-success'/>"
              method="post"
              class="m-0"
            >
              <button type="submit" class="btn btn-logout">
                Logout
              </button>
            </form>
          </div>
        </div>

      </div>
    </div>
  </div>
</body>
</html>
