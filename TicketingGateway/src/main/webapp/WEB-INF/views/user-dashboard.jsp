<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>User Dashboard</title>
  <!-- make sure you’re including Bootstrap CSS -->
  <link
    rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
  />
</head>
<body>
  <div class="container py-5">
    <div class="card mx-auto" style="max-width: 800px;">
      <div class="card-body text-center">
        <h2 class="card-title mb-3">User Dashboard</h2>
        <p class="mb-4">Hi, ${user.username}. You are logged in as <strong>user</strong>.</p>

        <div class="row gx-3 gy-2 justify-content-center">
          <div class="col-12 col-md-4">
            <button
              class="btn btn-primary w-100"
              onclick="location.href='${pageContext.request.contextPath}/user/create-ticket'"
            >
              Create Ticket
            </button>
          </div>
          <div class="col-12 col-md-4">
            <button
              class="btn btn-primary w-100"
              onclick="location.href='${pageContext.request.contextPath}/user/view-tickets'"
            >
              View Tickets
            </button>
          </div>
          <div class="col-12 col-md-4 position-relative">
            <button
              class="btn btn-primary w-100 position-relative"
              onclick="location.href='${pageContext.request.contextPath}/user/notifications'"
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

        <button
          class="btn btn-secondary w-50"
          onclick="location.href='${pageContext.request.contextPath}/logout'"
        >
          Logout
        </button>
      </div>
    </div>
  </div>

  <!-- optionally include Bootstrap JS bundle -->
  <script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
  ></script>
</body>
</html>
