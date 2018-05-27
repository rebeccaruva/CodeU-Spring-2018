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

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <!-- headings and links -->
  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%= request.getSession().getAttribute("user") %>!</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activity-feed">Activity Feed</a>
  </nav>

  <!-- prototype text -->
  <h1> Activity Feed </h1>

  <!-- check if user logged in -->
  <div id="container">
    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <% List<Activity> activities = (List<Activity>) request.getAttribute("all_activities");
      if(activities == null || activities.isEmpty()){ %>
        <p>The activity feed is currently empty. Start a conversation!</p>
      <% } else { %>
        <ul class = "mdl-list">
          <!-- lists all activities -->
          <% for(int index = activities.size() - 1; index >= 0; index--){ %>
            <% Activity activity = activities.get(index); int type = activity.getType(); %>
            <% if(activity.getType() == 1 && activities.indexOf(activity) == index){
                    type = 0;
            } %>
            <% if(type == 0){ %>
                <li>New user registered: welcome <strong><%= activity.getTitle() %></strong>!</li>
            <% } else if(type == 1){ %>
                <li><strong><%= activity.getTitle() %></strong> has logged in!</li>
            <% } else if(type == 2){ %>
                <li><strong><%= activity.getUser() %></strong> has started a conversation: <a href="/chat/<%= activity.getTitle() %>"><%= activity.getTitle() %></a>.</li>
            <% } else if(type == 3){%>
                <li><strong><%= activity.getUser() %></strong> wrote in <a href="/chat/<%=activity.getConversation() %>"><%= activity.getConversation() %></a>: <em><%= activity.getTitle() %></em></li>
            <% } %>
          <% } %>
        </ul>
    <% }
    } else{ %>
      <p>Please login to view the activity feed.</p>
    <% } %>
  </div>

</body>
</html>
