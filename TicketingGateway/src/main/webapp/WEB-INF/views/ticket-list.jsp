<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<head>
	<head>
		<c:if test="${activeRole == 'MANAGER'}">
		    <title>Tickets to Approve</title>
		</c:if>
		<c:if test="${activeRole == 'USER'}">
		    <title>Your Tickets</title>
		</c:if>
		<c:if test="${activeRole == 'ADMIN'}">
			<title>Tickets to Resolve</title>
		</c:if>
	</head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<div>
	<c:if test="${activeRole == 'MANAGER'}">
	    <h2>Tickets to Approve</h2>
	</c:if>
	<c:if test="${activeRole == 'USER'}">
	    <h2>Your Tickets</h2>
	</c:if>
	<c:if test="${activeRole == 'ADMIN'}">
	    <h2>Tickets to Resolve</h2>
	</c:if>
	<div id="assignSuccessMessage" style="display:none; color:green; font-weight:bold;">Assigned successfully!</div>
	<div id="assignErrorMessage" style="display:none; color:red; font-weight:bold;">Failed to assign.</div>



	<p></p>
	<c:choose>
	  <c:when test="${not empty ticketList}">
	<table border="1" width="100%" cellpadding="8">
	    <thead>
	        <tr>
	            <th>ID</th>
	            <th>Title</th>
	            <th>Priority</th>
	            <th>Status</th>
	            <th>Assignee</th>
	            <th>Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	    <c:forEach var="ticket" items="${ticketList}" varStatus="status">
	        <tr>
	            <td>${status.index + 1}</td>
	            <td>${ticket.title}</td>
	            <td>${ticket.priority}</td>
	            <td>${ticket.status}</td>
	            <td><c:out value="${ticket.assignee != null ? ticket.assignee : 'N/A'}" /></td>
				<td class="ticket-actions">
				    <div class="action-buttons">
				        <c:if test="${activeRole == 'USER'}">
				            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/view-ticket/${ticket.id}'">View</button>
				            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/update-ticket/${ticket.id}'">Update</button>
				            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/user/ticket-history/${ticket.id}'">History</button>
				        </c:if>

				        <c:if test="${activeRole == 'MANAGER'}">
							<div class="action-row">
							    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/view-ticket/${ticket.id}'">View</button>
							    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/manager/ticket-history/${ticket.id}'">History</button>

							    <button type="button" class="assign-toggle-btn" onclick="toggleAssign(${ticket.id})">Assign</button>

							    <form id="assignForm-${ticket.id}"
							          action="${pageContext.request.contextPath}/manager/assign-ticket/${ticket.id}"
							          method="post"
							          style="display: none;"
							          onsubmit="return submitAssign(${ticket.id});"
							          class="assign-form">
									  <select name="assignee" required>
									    <option value="" disabled selected hidden>Select Assignee</option>
									    <c:forEach var="admin" items="${admins}">
									      <option value="${admin.name}">${admin.name}</option>
									    </c:forEach>
									  </select>
							      <button type="submit">Submit</button>
							    </form>
							  </div>
				        </c:if>

				        <c:if test="${activeRole == 'ADMIN'}">
				            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/admin/view-ticket/${ticket.id}'">View</button>
				            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/admin/ticket-history/${ticket.id}'">History</button>
				        </c:if>
				    </div>
				</td>

	        </tr>
	    </c:forEach>
	    </tbody>
	</table>
	</c:when>
	 <c:otherwise>
		<c:if test="${activeRole == 'USER'}">
			<p style="color: red;">
				No tickets have been created.
			</p>
		</c:if>
		<c:if test="${activeRole == 'ADMIN'}">
			<p style="color: red;">
				No tickets to resolve.
			</p>
		</c:if>
		<c:if test="${activeRole == 'MANAGER'}">
			<p style="color: red;">
				No tickets to approve.
			</p>
		</c:if>
	  </c:otherwise>
	</c:choose>
</div>
<script>
	function toggleAssign(ticketId) {
	  const form = document.getElementById('assignForm-' + ticketId);
	  if (form) {
	    form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'flex' : 'none';
	  }
	}
	function submitAssign(ticketId) {
	  const $form = $('#assignForm-' + ticketId);
	  const assignee = $form.find('select[name="assignee"]').val();
	  const url = $form.attr('action');

	  if (!assignee) {
	    $('#assignErrorMessage').text("Please select an assignee.").fadeIn().delay(3000).fadeOut();
	    return false;
	  }

	  $.ajax({
	    url: url,
	    method: 'POST',
	    data: { assignee: assignee },
	    success: function () {
	      $form.hide();
	      $('#assignSuccessMessage').fadeIn().delay(3000).fadeOut();
	    },
	    error: function () {
	      $('#assignErrorMessage').text("Failed to assign. Try again.").fadeIn().delay(3000).fadeOut();
	    }
	  });

	  return false;
	}

</script>





