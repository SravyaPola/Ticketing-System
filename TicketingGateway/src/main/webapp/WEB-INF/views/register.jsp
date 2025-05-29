<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Register</title>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/theme.css"/>
</head>
<body>
  <div class="form-container">
    <header>
      <h1 class="form-title">Register</h1>
    </header>

    <!-- Global error -->
    <c:if test="${not empty error}">
      <div class="form-message error">${error}</div>
    </c:if>

    <form:form method="post"
               modelAttribute="registerDto"
               action="${pageContext.request.contextPath}/register"
               cssClass="auth-form">

      <div class="form-group">
        <label for="name">Username <span class="required">*</span></label>
        <form:input path="name"
                    id="name"
                    required="required"/>
        <form:errors path="name"
                     cssClass="form-message error"/>
      </div>

      <div class="form-group">
        <label for="email">Email <span class="required">*</span></label>
        <form:input path="email"
                    id="email"
                    type="email"
                    required="required"/>
        <form:errors path="email"
                     cssClass="form-message error"/>
      </div>

      <div class="form-group">
        <label for="password">Password <span class="required">*</span></label>
        <form:password path="password"
                       id="password"
                       required="required"/>
        <form:errors path="password"
                     cssClass="form-message error"/>
      </div>

      <div class="form-group">
        <label for="department">Department <span class="required">*</span></label>
        <form:input path="department"
                    id="department"
                    required="required"/>
        <form:errors path="department"
                     cssClass="form-message error"/>
      </div>

      <div class="form-group">
        <label for="project">Project <span class="required">*</span></label>
        <form:input path="project"
                    id="project"
                    required="required"/>
        <form:errors path="project"
                     cssClass="form-message error"/>
      </div>

      <div class="form-group">
        <label for="managerId">Manager <span class="required">*</span></label>
        <form:select path="managerId"
                     id="managerId"
                     required="required">
          <form:option value="" label="-- Select Manager --"/>
          <form:options items="${managers}"
                        itemValue="id"
                        itemLabel="name"/>
        </form:select>
        <form:errors path="managerId"
                     cssClass="form-message error"/>
      </div>

      <button type="submit"
              class="btn btn--full">
        Register
      </button>
    </form:form>

    <div class="form-footer">
      <a href="${pageContext.request.contextPath}/login">
        Already have an account? Login
      </a>
    </div>
  </div>
</body>
</html>
