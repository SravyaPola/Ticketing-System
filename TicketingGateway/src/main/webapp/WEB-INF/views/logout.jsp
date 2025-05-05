<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Logout</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container" style="text-align: center; padding: 60px 40px;">
        <h2>You have been logged out.</h2>
        <h3>Thanks for visiting.</h3>
        <a href="<c:url value='/login' />">Login again</a>
    </div>
</body>
</html>
