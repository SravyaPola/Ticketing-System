<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <c:if test="${not empty errorMsg}">
            <div class="errorMsg">${errorMsg}</div>
        </c:if>
        <form:form method="post" modelAttribute="loginDto" action="/login">
            <label>Username:</label>
            <form:input path="username" />
            <label>Password:</label>
            <form:password path="password" />
            <input type="submit" value="Login" />
        </form:form>
        <a href="/register">Don't have an account? Register</a>
    </div>
</body>
</html>
