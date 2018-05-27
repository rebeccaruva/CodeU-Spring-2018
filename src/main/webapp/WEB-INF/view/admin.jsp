<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>

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
    <a href="/activity-feed">Activity Feed</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
    <% if(request.getSession().getAttribute("user").equals("jordan")){ %>
      <a href="/admin">Admin</a>
    <% }} %>
  </nav>

<% List<Conversation> conversations =
  (List<Conversation>) request.getAttribute("conversations");
  int numConversations = 0;
  if (conversations != null){
     numConversations = conversations.size();
  } %>

<div id="container">
  <div
    style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
    <h1>Administration</h1>
      <h2>Statistics</h2>
          <a>Number of Conversations: <%= numConversations %></a>
  </div>
</div>

</body>
</html>
