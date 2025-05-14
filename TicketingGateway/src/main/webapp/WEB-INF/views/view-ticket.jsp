<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<head>
   <title>View Ticket</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div style="border: 1px solid #ccc; padding: 16px;">

   <c:if test="${not empty message}">
      <div class="message-box">${message}</div>
   </c:if>

   <h3>Ticket #${ticket.id}: "${ticket.title}"</h3>
   <hr/>

   <p><strong>Status:</strong> ${ticket.status}</p>
   <p><strong>Priority:</strong> ${ticket.priority}</p>
   <p><strong>Category:</strong> ${ticket.category}</p>
   <p><strong>Created By:</strong> ${ticket.createdBy}</p>
   <p><strong>Assignee:</strong> ${ticket.assignee}</p>
   <p><strong>Created On:</strong> ${ticket.creationDate}</p>
   <hr/>

   <p><strong>Description:</strong><br/>
   <blockquote>${ticket.description}</blockquote></p>
   <p><strong>Attachments to Download:</strong>
	<c:forEach var="filePath" items="${ticket.fileAttachments}">
	    <c:set var="segments" value="${fn:split(filePath, '/')}"/>
	    <c:set var="filename" value="${segments[fn:length(segments) - 1]}"/>
		<p>Files = ${ticket.fileAttachments}</p>
	    <li>
	        <a href="${pageContext.request.contextPath}/download?path=${filePath}">
	            <c:out value="${filename}" />
	        </a>
	    </li>
	</c:forEach>

	</ul>
   <hr/>

   <c:if test="${activeRole == 'USER'}">
     <div class="inline-button-group" id="actionButtons">
			    <form id="closeForm" method="post" action="${pageContext.request.contextPath}/user/close-ticket/${ticket.id}">
			        <button type="submit" class="action-button">Close</button>
			    </form>
				<form id="reopenForm" method="post" action="${pageContext.request.contextPath}/user/reopen-ticket/${ticket.id}">
					<button type="submit" class="action-button">ReOpen</button>
				</form>
		</div>
   </c:if>

   <c:if test="${activeRole == 'MANAGER'}">
	<div class="inline-button-group" id="actionButtons">
	    <form id="approveForm" method="post" action="${pageContext.request.contextPath}/manager/approve-ticket/${ticket.id}">
	        <button type="submit" class="action-button">Approve</button>
	    </form>

	    <form id="rejectToggleForm" onsubmit="return false;">
	        <button type="button" class="action-button"
	                onclick="showRejectForm()">Reject</button>
	    </form>
	</div>
	<form id="rejectForm"
	      method="post"
	      action="${pageContext.request.contextPath}/manager/reject-ticket/${ticket.id}"
	      style="display: none; margin-top: 12px; text-align: center;">
	    <label for="reason"><strong>Reject Reason:</strong></label><br/>
	    <input type="text" name="reason" id="reason"
	           required
	           style="width: 100%; max-width: 400px; padding: 6px 8px; margin-top: 6px;" />
	    <br/>
	    <button type="submit" style="margin-top: 10px;">Submit Reject</button>
	</form>

   </c:if>

   <c:if test="${activeRole == 'ADMIN'}">
      <div class="button-group">
         <button type="button">Resolve</button>
         <button type="button">Open</button>
         <button type="button">Close</button>
      </div>
   </c:if>

</div>
</body>
<script>
    function showRejectForm() {
        document.getElementById('rejectForm').style.display = 'block';
        document.getElementById('rejectToggleForm').style.display = 'none';
        document.getElementById('approveForm').style.display = 'none';
    }
</script>



