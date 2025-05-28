<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Manager Notifications</title>
  <link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
    rel="stylesheet"
  />
  <style>
    /* make the heading smaller */
    .page-title {
      font-size: 1.75rem;      /* about an H2 size */
      font-weight: 500;
      margin-bottom: 1rem;
      text-align: center;
    }
    /* narrow the card column */
    .notifications-col {
      max-width: 600px;
      width: 100%;
    }
    .notification {
      /* you could also use .card here */
      border: 1px solid #ddd;
      border-radius: .25rem;
      padding: 1rem;
      margin-bottom: .75rem;
      background-color: #fff;
    }
  </style>
</head>
<body>
  <div class="container py-4">
    <div class="row justify-content-center">
      <div class="notifications-col">
        
        <!-- smaller, centered heading -->
        <h1 class="page-title">Manager Notifications</h1>
        
        <c:choose>
          <c:when test="${empty notifications}">
            <p class="text-center">No notifications to display.</p>
          </c:when>
          <c:otherwise>
            <ul class="list-unstyled">
              <c:forEach var="n" items="${notifications}">
                <li class="notification">
                  <p class="mb-1"><strong>Time:</strong> <c:out value="${n.receivedAt}"/></p>
                  <p class="mb-0"><strong>Message:</strong> <c:out value="${n.message}"/></p>
                </li>
              </c:forEach>
            </ul>
          </c:otherwise>
        </c:choose>
        
        <!-- buttons below the notifications -->
		<!-- replace your old <div class="d-flex …">…</div> with this: -->
		<div class="row row-cols-1 row-cols-sm-2 g-3 mt-4 justify-content-center">
		  <div class="col notifications-btn-col">
		    <button
		      type="button"
		      class="btn btn-primary w-100"
		      onclick="location.href='${pageContext.request.contextPath}/manager/home'"
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
		      <button
		        type="submit"
		        class="btn btn-secondary w-100"
		      >
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
