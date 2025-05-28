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
   <p><strong>Assignee:</strong>
     <c:if test="${not empty ticket.assignee}">
       ${ticket.assignee}
     </c:if>
     <c:if test="${empty ticket.assignee}">
       Not yet assigned
     </c:if>
   </p>
   <p><strong>Created On:</strong> ${ticket.creationDate}</p>
   <hr/>

   <p><strong>Description:</strong><br/>
   <blockquote>${ticket.description}</blockquote></p>
	<c:if test="${not empty ticket.fileAttachments}">
	    <strong>Attachments to Download:</strong>
	    <ul>
	        <c:forEach var="filePath" items="${ticket.fileAttachments}">
	            <c:if test="${not empty filePath}">
	                <c:set var="segments" value="${fn:split(filePath, '/')}"/>
	                <c:set var="filename" value="${segments[fn:length(segments) - 1]}"/>
	                <li>
	                    <a href="${pageContext.request.contextPath}/download?path=${filePath}" download>
	                        <c:out value="${filename}" />
	                    </a>
	                </li>
	            </c:if>
	        </c:forEach>
	    </ul>
	</c:if>


	</ul>
   <hr/>

   <c:if test="${activeRole eq 'USER' and ticket.status eq 'RESOLVED'}">
	<div id="actionButtons"
	         class="d-flex flex-column gap-2 mb-4">
	      <form method="post"
	            action="${pageContext.request.contextPath}/user/close-ticket/${ticket.id}"
	            class="m-0">
	        <button type="submit"
	                class="btn btn-secondary w-100">
	          Close
	        </button>
	      </form>
	    </div>
   </c:if>
   <c:if test="${activeRole eq 'USER' and ticket.status eq 'CLOSED'}">
	<div id="actionButtons"
		         class="d-flex flex-column gap-2 mb-4">
		      <form method="post"
		            action="${pageContext.request.contextPath}/user/reopen-ticket/${ticket.id}"
		            class="m-0">
		        <button type="submit"
		                class="btn btn-secondary w-100">
		          ReOpen
		        </button>
		      </form>
		    </div>
   </c:if>
   <c:if test="${activeRole == 'MANAGER'}">
	<div id="actionButtons"
			         class="d-flex flex-column gap-2 mb-4">
		<form id="approveForm"
		      method="post"
		      action="${pageContext.request.contextPath}/manager/approve-ticket/${ticket.id}"
		      class="m-0">
		  <button type="submit"
		          class="btn btn-secondary w-100">
		    Approve
		  </button>
		</form>

		<form id="rejectToggleForm"
		      onsubmit="return false;"
		      class="m-0">
		  <button type="button"
		          class="btn btn-secondary w-100"
		          onclick="showRejectForm()">
		    Reject
		  </button>
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

   <c:if test="${activeRole eq 'ADMIN'}">
     <div id="actionButtons"
          class="d-flex flex-column gap-2 mb-4">
       <button type="button"
               id="startResolveBtn"
               class="btn btn-success w-100"
               onclick="showResolveDetails()">
         Resolve
       </button>
       <form method="post"
             action="${pageContext.request.contextPath}/admin/close-ticket/${ticket.id}"
             class="m-0">
         <button type="submit"
                 class="btn btn-secondary w-100">
           Close
         </button>
       </form>
       <form method="post"
             action="${pageContext.request.contextPath}/admin/reopen-ticket/${ticket.id}"
             class="m-0">
         <button type="submit"
                 class="btn btn-secondary w-100">
           ReOpen
         </button>
       </form>
     </div>
     <form id="resolveDetailsForm"
           method="post"
           action="${pageContext.request.contextPath}/admin/resolve-ticket/${ticket.id}"
           style="display:none;"
           class="mb-4">
       <div class="mb-3">
         <textarea name="resolutionComment"
                   class="form-control"
                   rows="4"
                   placeholder="Enter resolution comments"
                   required></textarea>
       </div>
       <button type="submit"
               class="btn btn-success w-100">
         Submit Resolve
       </button>
     </form>
   </c:if>

</div>
</body>
<script>
    function showRejectForm() {
        document.getElementById('rejectForm').style.display = 'block';
        document.getElementById('rejectToggleForm').style.display = 'none';
        document.getElementById('approveForm').style.display = 'none';
    }
	function showResolveDetails() {
	    document.getElementById('startResolveBtn').style.display = 'none';
	    document.getElementById('resolveDetailsForm').style.display = 'block';
	    document.querySelector('#resolveDetailsForm textarea').focus();
	  }
</script>



