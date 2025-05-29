<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Select Role</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css" />
</head>
<body>
  <div class="form-container">
    
    <header>
      <h1 class="form-title">Welcome, ${username}</h1>
    </header>
    
    <c:if test="${not empty error}">
      <div class="form-message error">${error}</div>
    </c:if>
    
    <hr />
    
    <p class="form-text">Login as:</p>
    
    <form:form method="post" modelAttribute="roleForm" cssClass="auth-form">
    
      <div class="form-group">
        <input 
          type="submit" 
          name="roleChoice" 
          value="USER" 
          class="btn btn--full" 
        />
      </div>
      
      <div class="form-group">
        <input 
          type="submit" 
          name="roleChoice" 
          value="ADMIN" 
          class="btn btn--full" 
        />
      </div>
      
      <div class="form-group">
        <input 
          type="submit" 
          name="roleChoice" 
          value="MANAGER" 
          class="btn btn--full" 
        />
      </div>
      
    </form:form>
    
  </div>
</body>
</html>
