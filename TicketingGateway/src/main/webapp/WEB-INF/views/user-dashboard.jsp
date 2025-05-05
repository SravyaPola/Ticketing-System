<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>User Dashboard</h2>
        <p>Hi, ${name}. You are logged in as user.</p>
        <form action="<c:url value='/logout-success' />" method="post">
            <button type="submit">Logout</button>
        </form>
    </div>
</body>
</html>
