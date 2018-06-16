<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <!-- headings and links -->
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

  <!-- check if user logged in -->
  <div id="container">
    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <h1>Activity Feed</h1>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <!-- get list of activities -->
      <% List<Activity> activities = (List<Activity>) request.getAttribute("all_activities");
      if(activities == null || activities.isEmpty()){ %>
        <p>The activity feed is currently empty. Start a conversation!</p>
      <% } else { %>
        <ul class = "mdl-list">
          <!-- lists all activities in reverse chronological order -->
          <% for(int index = activities.size() - 1; index >= 0; index--){ %>
            <% Activity activity = activities.get(index); Activity.Type type = activity.getType(); %>
            <% if(type == Activity.Type.REGISTERED){ %>
                <!-- new user registered -->
                <li><strong><font size = "4"><%= activity.formattedTime() %>: </font></strong>New user registered: welcome <%= ((User)(activity.getUser())).getName() %>!</li>
            <% } else if(type == Activity.Type.LOGGED_IN){ %>
                <!-- user logged in -->
                <li><strong><font size = "4"><%= activity.formattedTime() %>: </font></strong><%= ((User)(activity.getUser())).getName() %> has logged in!</li>
            <% } else if(type == Activity.Type.NEW_CONVERSATION){ %>
                <!-- new conversation created -->
                <li><strong><font size = "4"><%= activity.formattedTime() %>: </font></strong><%= ((Conversation)(activity.getConversation())).getUser() %> has started a conversation: <a href="/chat/<%= ((Conversation)(activity.getConversation())).getTitle() %>"><%= ((Conversation)(activity.getConversation())).getTitle() %></a>.</li>
            <% } else if(type == Activity.Type.NEW_MESSAGE){%>
                <!-- new message sent -->
                <li><strong><font size = "4"><%= activity.formattedTime() %>: </font></strong><%= ((Message)(activity.getMessage())).getUser() %> sent a message in <a href="/chat/<%= ((Message)(activity.getMessage())).getConversation() %>"><%= ((Message)(activity.getMessage())).getConversation() %></a>: <em><%= ((Message)(activity.getMessage())).getContent() %></em></li>
            <% } %>
          <% } %>
        </ul>
    <% }
  } else{ %> <!-- user not logged in -->
      <p>Please login to view the activity feed.</p>
    <% } %>
  </div>
</body>
</html>
