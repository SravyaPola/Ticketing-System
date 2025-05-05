<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Select Role</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>Welcome, ${username}</h2>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form:form method="post" modelAttribute="roleForm">
            <input type="submit" name="roleChoice" value="USER" />
            <input type="submit" name="roleChoice" value="ADMIN" />
            <input type="submit" name="roleChoice" value="MANAGER" />
        </form:form>
    </div>
</body>
</html>
