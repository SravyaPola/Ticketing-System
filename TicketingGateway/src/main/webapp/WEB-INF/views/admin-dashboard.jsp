<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Admin Dashboard</title>
  <!-- your existing custom styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <!-- Bootstrap for the same look & feel -->
  <link
    rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
  />
</head>
<body>
  <div class="container py-5">
    <div class="card mx-auto" style="max-width: 800px;">
      <div class="card-body text-center">
        <h2 class="card-title mb-3">Admin Dashboard</h2>
        <p class="mb-4">
          Hi, ${name}. You are logged in as <strong>admin</strong>.
        </p>

        <!-- two-button row -->
        <div class="row gy-3">
          <!-- View Tickets -->
          <div class="col-12 col-md-6">
            <button
              class="btn btn-primary w-100"
              onclick="location.href='${pageContext.request.contextPath}/admin/tickets-to-resolve'"
            >
              View Tickets
            </button>
          </div>

          <!-- View Notifications with badge -->
          <div class="col-12 col-md-6 position-relative">
            <button
              class="btn btn-primary w-100 position-relative"
              onclick="location.href='${pageContext.request.contextPath}/admin/notifications'"
            >
              View Notifications
              <c:if test="${unreadCount > 0}">
                <span
                  class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
                >
                  ${unreadCount}
                  <span class="visually-hidden">unread notifications</span>
                </span>
              </c:if>
            </button>
          </div>
        </div>

        <hr class="my-4"/>

        <!-- Logout -->
		<!-- Logout button, centered -->
		<form action="<c:url value='/logout-success'/>" method="post">
		  <button 
		    type="submit" 
		    class="btn btn-secondary w-50 d-block mx-auto mt-4"
		  >
		    Logout
		  </button>
		</form>


      </div>
    </div>
  </div>

  <!-- Bootstrap JS (optional) -->
  <script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
  ></script>
</body>
</html>
