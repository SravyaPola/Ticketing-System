<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <form:form method="post" modelAttribute="registerDto">
        <h2>Register</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <label>Username:</label>
        <form:input path="name" />

        <label>Email:</label>
        <form:input path="email" />

        <label>Password:</label>
        <form:password path="password" />
		<!-- 
        <label>Roles:</label>
        <div class="checkbox-group">
            <form:checkbox path="roles" value="USER" label="USER" />
            <form:checkbox path="roles" value="ADMIN" label="ADMIN" />
            <form:checkbox path="roles" value="MANAGER" label="MANAGER" />
        </div>
		-->
        <label>Department:</label>
        <form:input path="department" />

        <label>Project:</label>
        <form:input path="project" />

		<label for="managerId">Manager:</label>
		<form:select path="managerId" id="managerId">
		  <form:option value="" label="-- Select Manager --" />
		  <!-- 
		       items="${managers}" 
		       itemValue="id"     → the value submitted (manager.id)
		       itemLabel="name"   → the text shown in each <option>
		  -->
		  <form:options items="${managers}" itemValue="id" itemLabel="name" />
		</form:select>
		<form:errors path="managerId" cssClass="error" />

        <input type="submit" value="Register" />
        <a href="/login">Already have an account? Login</a>
    </form:form>
</body>
</html>
