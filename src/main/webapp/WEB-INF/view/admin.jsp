<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<!DOCTYPE html>
<html>
<head>
  <title>Admin</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
  <link rel="icon" href="/favicon.ico" type="image/x-icon">
  <style>

  </style>
</head>

<body>

  <nav>
    <a id="navTitle" href="/">IMhere!</a>
    <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
    <a href="/activity-feed">Activity Feed</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/notifications">Notifications</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <% if((request.getSession().getAttribute("user") != null) && request.getSession().getAttribute("adminStatus") != null){ %>
    <% if((Boolean)request.getSession().getAttribute("adminStatus").equals(true)){ %>
      <a href="/admin">Admin</a>
    <% }} %>
  </nav>

<!--Preparing data for admin statistics -->
<% List<Conversation> conversations =
  (List<Conversation>) request.getAttribute("conversations");
  int numConversations = 0;
  if (request.getAttribute("numConversations") != null){
     numConversations = (int)request.getAttribute("numConversations");
  }
  int numUsers = 0;
  if (request.getAttribute("numUsers") != null){
    numUsers = (int)request.getAttribute("numUsers");
  }
  int numMessages = 0;
  if (request.getAttribute("numMessages") != null){
    numMessages = (int)request.getAttribute("numMessages");
  }
  %>

<div id="container">
  <div
    style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
    <% if (request.getSession().getAttribute("user") == null){ %>
    <p> Please log in to access the administrator page. </p>
    <% } else if ((request.getSession().getAttribute("adminStatus") == null)
    || (Boolean)request.getSession().getAttribute("adminStatus").equals(false)){ %>
    <p> You do not have access to the administrator page. </p>
    <% } else { %>
    <h1>Administration</h1>
      <h2>Statistics</h2>
          <a>Number of Conversations: <%= numConversations %></a> </br>
          <a>Number of Users: <%= numUsers %></a> </br>
          <a>Number of Messages: <%= numMessages %></a> </br> </br>

      <% if(request.getAttribute("error") != null){ %>
          <h2 style="color:red"><%= request.getAttribute("error") %></h2>
      <% } %>

    <form action="/admin" method="POST">
      <label for="adminUsername">Give another user admin status:</label>
        <br/>
      <input type="text" name="adminUsername" id="adminUsername">
        <br/>
        <button type="submit">Submit</button>
    </form>

    <% if(request.getAttribute("success") != null){ %>
        <p><%= request.getAttribute("success") %></p>
    <% } %>
  </div>
  <% } %>
</div>

</body>
</html>
