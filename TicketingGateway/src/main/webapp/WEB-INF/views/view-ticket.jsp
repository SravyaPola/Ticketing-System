<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
  <title>View Ticket</title>
  <link
    rel="stylesheet"
    href="${pageContext.request.contextPath}/css/view-ticket.css"
  />
</head>
<body>
<div class="app-wrapper">
  <!-- Card Container -->
  <div class="ticket-card">

    <!-- Flash Message -->
    <c:if test="${not empty message}">
      <div class="message-box">${message}</div>
    </c:if>

    <!-- Header -->
	<div class="ticket-header">
	  <h3>Ticket #${ticket.id}: “${ticket.title}”</h3>
	  <hr/>
	</div>


    <!-- Details -->
    <div class="ticket-details">

      <p class="detail-item">
        <strong>Status:</strong>
        <span
          class="status status-${fn:toLowerCase(ticket.status).replace('_','-')}"
        >
          ${ticket.status}
        </span>
      </p>

      <p class="detail-item">
        <strong>Priority:</strong> ${ticket.priority}
      </p>

      <p class="detail-item">
        <strong>Category:</strong> ${ticket.category}
      </p>

      <p class="detail-item">
		<strong>Created By:</strong> ${ticket.createdBy}
      </p>

      <p class="detail-item">
		<strong>Assignee:</strong>
        <c:choose>
          <c:when test="${not empty ticket.assignee}">
            ${ticket.assignee}
          </c:when>
          <c:otherwise>Not yet assigned</c:otherwise>
        </c:choose>
      </p>

      <p class="detail-item">
		<strong>Created On:</strong> ${ticket.creationDate}
      </p>

    </div>

    <!-- Description Block -->
    <div class="ticket-description">
      <strong>Description:</strong>
      <blockquote>${ticket.description}</blockquote>
    </div>

    <!-- Attachments -->
    <c:if test="${not empty ticket.fileAttachments}">
      <div class="attachments">
        <strong>Attachments:</strong>
        <ul>
          <c:forEach var="filePath" items="${ticket.fileAttachments}">
            <c:if test="${not empty filePath}">
              <c:set var="segments" value="${fn:split(filePath,'/')}" />
              <c:set var="filename" value="${segments[fn:length(segments)-1]}" />
              <li>
                <a
                  href="${pageContext.request.contextPath}/download?path=${filePath}"
                  download
                >
                  ${filename}
                </a>
              </li>
            </c:if>
          </c:forEach>
        </ul>
      </div>
    </c:if>

	<div class="ticket-actions">
	  <!-- USER: Close or Reopen -->
	  <c:if test="${activeRole == 'USER' && ticket.status == 'RESOLVED'}">
	    <form
	      method="post"
	      action="${pageContext.request.contextPath}/user/close-ticket/${ticket.id}">
	      <button type="submit" class="btn btn-primary">
	        Close
	      </button>
	    </form>
	  </c:if>

	  <c:if test="${activeRole == 'USER' && ticket.status == 'CLOSED'}">
	    <form
	      method="post"
	      action="${pageContext.request.contextPath}/user/reopen-ticket/${ticket.id}">
	      <button type="submit" class="btn btn-secondary">
	        Reopen
	      </button>
	    </form>
	  </c:if>


	  <!-- MANAGER: Approve & Reject (only when OPEN or PENDING_FOR_APPROVAL) -->
	  <c:if test="${activeRole == 'MANAGER' && (ticket.status == 'OPEN' || ticket.status == 'PENDING_FOR_APPROVAL')}">
	    <!-- Approve button -->

		    <!-- Approve Form (unchanged) -->
		    <form id="approveForm"
		          method="post"
		          action="${pageContext.request.contextPath}/manager/approve-ticket/${ticket.id}">
		      <button type="submit" class="btn btn-primary">
		        Approve
		      </button>
		    </form>

		    <!-- Reject Container -->
		    <div class="reject-container">
		      <!-- Reject button -->
		      <button type="button"
		              class="btn btn-danger"
		              onclick="showRejectForm()">
		        Reject
		      </button>

		      <!-- Inline Reject form (hidden by default) -->
		      <form id="rejectForm"
		            method="post"
		            action="${pageContext.request.contextPath}/manager/reject-ticket/${ticket.id}"
		            class="ticket-form"
		            style="display:none;">
		        <label for="reason"><strong>Reason:</strong></label>
		        <input type="text" name="reason" id="reason" required />
		        <button type="submit" class="btn btn-danger">
		          Submit Reject
		        </button>
		      </form>
		    </div>

		  
	  </c:if>

	  <!-- ADMIN: Resolve / Close / Reopen -->
	  <c:if test="${activeRole == 'ADMIN'}">
	    <!-- Resolve toggle -->
	    <button type="button"
	            id="startResolveBtn"
	            class="btn btn-success"
	            onclick="showResolveDetails();">
	      Resolve
	    </button>

	    <!-- Inline Resolve form -->
	    <form id="resolveDetailsForm"
	          method="post"
	          action="${pageContext.request.contextPath}/admin/resolve-ticket/${ticket.id}"
	          style="display:none;"
	          class="ticket-form">
	      <textarea name="resolutionComment" rows="4" required
	                placeholder="Enter resolution comments"></textarea>
	      <button type="submit" class="btn btn-success">Submit Resolve</button>
	    </form>

	    <!-- Close -->
	    <form method="post"
	          action="${pageContext.request.contextPath}/admin/close-ticket/${ticket.id}">
	      <button type="submit" class="btn btn-secondary">Close</button>
	    </form>

	    <!-- Reopen -->
	    <form method="post"
	          action="${pageContext.request.contextPath}/admin/reopen-ticket/${ticket.id}">
	      <button type="submit" class="btn btn-secondary">Reopen</button>
	    </form>
	  </c:if>

	</div>
	</div>
	</div>
	<script>
	  function showRejectForm() {
	    document.getElementById('rejectForm').style.display = 'block';
	    document.getElementById('approveForm').style.display = 'none';
	  }
	  function showResolveDetails() {
	    document.getElementById('startResolveBtn').style.display = 'none';
	    document.getElementById('resolveDetailsForm').style.display = 'block';
	  }
	</script>