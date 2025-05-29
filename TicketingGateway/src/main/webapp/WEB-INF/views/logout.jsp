<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Logged Out</title>
  <!-- (Optional) Google Font -->
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,600&display=swap" rel="stylesheet">
  <style>
    body {
      margin: 0;
      padding: 0;
      font-family: 'Montserrat', sans-serif;
      background: #f5f5f5; /* plain light background */
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .logout-box {
      max-width: 500px;
      width: 90%;
      padding: 40px;
      border-radius: 12px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #ffffff;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
      text-align: center;
    }
    .logout-box .icon {
      font-size: 3em;
      margin-bottom: 20px;
      display: inline-block;
      transform: rotate(-15deg);
      opacity: 0.8;
    }
    .logout-box h2 {
      margin: 0 0 10px;
      font-size: 2em;
      font-weight: 600;
    }
    .logout-box p {
      margin: 0 0 30px;
      font-size: 1.1em;
      opacity: 0.9;
    }
    .logout-box a {
      display: inline-block;
      padding: 12px 28px;
      font-size: 1em;
      font-weight: 600;
      color: #764ba2;
      background: #ffffff;
      border-radius: 50px;
      text-decoration: none;
      transition: background 0.3s, color 0.3s;
    }
    .logout-box a:hover {
      background: #764ba2;
      color: #ffffff;
    }
  </style>
</head>
<body>
  <div class="logout-box">
    <h2>You Have Been Logged Out</h2>
    <p>Thanks for using our services. We hope to see you again soon!</p>
    <a href="<c:url value='/login' />">Log In Again</a>
  </div>
</body>
</html>
