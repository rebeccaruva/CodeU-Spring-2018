<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.Notification" %>
<%@ page import="codeu.model.store.basic.NotificationStore" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Notifications</title>
  <link rel="stylesheet" href="/css/main.css">
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

  <% List<Notification> unreadNotifications = (List<Notification>) request.getAttribute("unreadNotifications"); %>
  <% List<Notification> readNotifications = (List<Notification>) request.getAttribute("readNotifications"); %>
  <% int numUnreadNotifications = (int) request.getSession().getAttribute("numUnreadNotifications"); %>
  <% int numReadNotifications = (int) request.getSession().getAttribute("numReadNotifications"); %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <% if(numUnreadNotifications==0){ %>
        <h1>Hi <%= request.getSession().getAttribute("user") %>, you have
        no new notifications.</h1>
      <% } else if(numUnreadNotifications==1){%>
        <h1>Hi <%= request.getSession().getAttribute("user") %>, you have
        1 new notification.</h1>
      <% } else{ %>
        <h1>Hi <%= request.getSession().getAttribute("user") %>, you have
        <%= numUnreadNotifications %> new notifications.</h1>
      <% } %>

      <% User user = UserStore.getInstance().getUser(request.getSession().getAttribute("user").toString()); %>

      <% if(numUnreadNotifications != 0){ %>
      <h2>New Notifications</h2>
      <div id="chat">
        <ul>
      <%
        for (Notification notification : unreadNotifications) {
          Message message = (Message) notification.getMessage();
          String messageAuthor = message.getUser();
          String conversationName = message.getConversation();
          String messageConversationLink = "/chat/" + conversationName;
          NotificationStore.getInstance().markNotificationAsViewed(notification);
      %>
        <li><strong><%= messageAuthor %></strong> mentioned you in <a href=<%= messageConversationLink %>><%= conversationName %></a>: "<%= message.getTranslationAndAdd(user.getLanguagePreference()) %>"</li>
      <%
        }
      %>
        </ul>
      </div>

      <% } %>

      <% if(numReadNotifications != 0){ %>
      <h2>Read Notifications</h2>
      <div id="chat">
        <ul>
      <%
        for (Notification notification : readNotifications) {
          Message message = (Message) notification.getMessage();
          String messageAuthor = message.getUser();
          String conversationName = message.getConversation();
          String messageConversationLink = "/chat/" + conversationName;
      %>
        <li><strong><%= messageAuthor %></strong> mentioned you in <a href=<%= messageConversationLink %>><%= conversationName %></a>: "<%= message.getTranslationAndAdd(user.getLanguagePreference()) %>"</li>
      <%
        }
      %>
        </ul>
      </div>

      <% } %>

      <% if(numReadNotifications > 0 || numUnreadNotifications > 0) { %>
      <form action="/notifications" method="POST">
        <button type="submit">Clear All</button>
      </form>
      <% } %>

    <% } else{%>
        <h2 style="color:red">Error: Please <a href="/login">login</a> to access notifications.</h2>
    <%}%>
  </div>
</body>
</html>
