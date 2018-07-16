<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Notification" %>
<%@ page import="codeu.model.store.basic.NotificationStore" %>

<!DOCTYPE html>
<html>
<head>
  <title>Notifications</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <% int numNotifications = 0; %>
  <% if(request.getSession().getAttribute("user") != null){ %>
    <% numNotifications = (int) request.getSession().getAttribute("numNotifications"); %>
  <% } %>

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

  <% List<Notification> notifications = (List<Notification>) request.getAttribute("notifications"); %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <% if(numNotifications==1){ %>
        <h1>Hi <%= request.getSession().getAttribute("user") %>, you have
        <%= numNotifications %> notification.</h1>
      <% } else{ %>
        <h1>Hi <%= request.getSession().getAttribute("user") %>, you have
        <%= numNotifications %> notifications.</h1>
      <% } %>

     <div id="chat">
        <ul>
      <%
        for (Notification notification : notifications) {
          Message message = (Message) notification.getMessage();
          String messageAuthor = message.getUser();
          String messageConversationLink = "/chat/" + message.getConversation();
          NotificationStore.getInstance().markNotificationAsViewed(notification);
      %>
        <li><strong><%= messageAuthor %> mentioned you: </strong>"<a href=<%= messageConversationLink %>><%= message.getContent() %>"</a></li>
      <%
        }
      %>
        </ul>
      </div>

    <h2>Statistics</h2>

    <p> Statistics here: 123 </p>
    <% } else{%>
        <h2 style="color:red">Error: Please log in to access notifications.</h2>
    <%}%>
  </div>
</body>
</html>
