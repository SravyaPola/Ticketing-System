<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/theme.css" />
</head>
<body>
  <div class="auth-card">

    <!-- LEFT: Login Form -->
    <div class="auth-card__left">
      <h2 class="auth-title">Login</h2>

      <c:if test="${not empty errorMsg}">
        <div class="form-message error">${errorMsg}</div>
      </c:if>

      <form:form method="post"
                 modelAttribute="loginDto"
                 action="${pageContext.request.contextPath}/login"
                 cssClass="auth-form">

        <div class="form-group">
          <label for="username">Username</label>
          <form:input path="username" id="username" />
          <form:errors path="username"
                       cssClass="form-message error"
                       element="div" />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <form:password path="password" id="password" />
          <form:errors path="password"
                       cssClass="form-message error"
                       element="div" />
        </div>

        <button type="submit"
                class="btn btn--full">
          Log In
        </button>
      </form:form>
    </div>

    <!-- RIGHT: Register Call-Out -->
    <div class="auth-card__right">
      <h2 class="auth-title">Register</h2>
      <p class="auth-text">Don’t have an account? Register one!</p>
      <a href="${pageContext.request.contextPath}/register"
         class="btn btn--full">
        Register an Account
      </a>
    </div>

  </div>
</body>
</html>
