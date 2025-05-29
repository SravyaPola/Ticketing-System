<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>User Dashboard</title>
  <link
    rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
  />
  <link
    rel="stylesheet"
    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
  />
  <link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/dashboard.css"
  />
</head>
<body>
  <div class="dashboard-container">
    <div class="dashboard-card">

      <!-- Header -->
      <div class="dashboard-header">
        <h2>User Dashboard</h2>
        <p>Hi, ${name}. You are logged in as <strong>user</strong>.</p>
      </div>

      <!-- Service Grid -->
      <div class="service-grid">

        <!-- Create Ticket -->
        <div class="service-item">
          <a href="${pageContext.request.contextPath}/user/create-ticket">
            <div class="icon-circle">
              <i class="bi bi-ticket-detailed"></i>
            </div>
            <h5>Create Ticket</h5>
          </a>
        </div>

        <!-- View Tickets -->
        <div class="service-item">
          <a href="${pageContext.request.contextPath}/user/ticket-list">
            <div class="icon-circle">
              <i class="bi bi-card-list"></i>
            </div>
            <h5>View Tickets</h5>
          </a>
        </div>

        <!-- View Notifications -->
        <div class="service-item">
          <a href="${pageContext.request.contextPath}/user/notifications">
            <div class="icon-circle">
              <i class="bi bi-bell"></i>
              <c:if test="${unreadCount > 0}">
                <span class="badge-notify">${unreadCount}</span>
              </c:if>
            </div>
            <h5>Notifications</h5>
          </a>
        </div>

        <!-- Logout -->
        <div class="service-item">
          <form action="<c:url value='/logout-success'/>" method="post">
            <div class="icon-circle">
              <i class="bi bi-box-arrow-right"></i>
            </div>
            <button type="submit">
              <h5>Logout</h5>
            </button>
          </form>
        </div>

      </div>
    </div>
  </div>

  <script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
  ></script>
</body>
</html>
