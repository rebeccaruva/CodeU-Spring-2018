<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

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
    <% if((request.getSession().getAttribute("user") != null) && request.getSession().getAttribute("admin") != null){ %>
    <% if((Boolean)request.getSession().getAttribute("admin").equals(true)){ %>
      <a href="/admin">Admin</a>
    <% }} %>
  </nav>

<!--Preparing data for admin statistics -->
<% List<Conversation> conversations =
  (List<Conversation>) request.getAttribute("conversations");
  int numConversations = 0;
  if (conversations != null){
     numConversations = conversations.size();
  }
  int numUsers = 0;
  if (request.getAttribute("numusers") != null){
    numUsers = (int)request.getAttribute("numusers");
  }
  int numMessages = 0;
  if (request.getAttribute("numMessages") != null){
    numMessages = (int)request.getAttribute("numMessages");
  }
  %>

<div id="container">
  <div
    style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">
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
</div>

</body>
</html>
