<!DOCTYPE html>
<html>
<head>
  <title>Admin</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>

  </style>
</head>

<body>

  <nav>
    <a id="navTitle" href="/">IMhere!</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/admin">Admin</a>
  </nav>



<div id="container">
  <div
    style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
    <% if(request.getSession().getAttribute("user") != null){ %>
    <% if(request.getSession().getAttribute("user").equals("jordan")){ %>
      <h1>Admin Stats</h1>
      <p>This is the administrator page. You are an administrator.</p>
      <%}else{%>
        <a>You do not have access to the administrator page because you are not an administrator.</a>
      <% }} else{%>
        <p>Please log in to access the administrator page.</p>
      <% } %>
  </div>
</div>

</body>
</html>
