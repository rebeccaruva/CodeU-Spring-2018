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
<!DOCTYPE html>
<html>
<head>
  <title>About</title>
  <link rel="stylesheet" href="/css/main.css">
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
  <link rel="icon" href="/favicon.ico" type="image/x-icon">
</head>
<body>

  <% int numUnreadNotifications = 0; %>
  <% if(request.getSession().getAttribute("user") != null){ %>
    <% numUnreadNotifications = (int) request.getSession().getAttribute("numUnreadNotifications"); %>
  <% } %>

  <nav>
    <a id="navTitle" href="/">IMhere!</a>
    <a href="/conversations">Conversations</a>
    <a href="/about.jsp">About</a>
    <a href="/activity-feed">Activity Feed</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <% if (numUnreadNotifications != 0) { %>
        <a href="/notifications">Notifications (<%= numUnreadNotifications %>)</a>
      <% } else { %>
        <a href="/notifications">Notifications</a>
      <% } %>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <% if((request.getSession().getAttribute("user") != null) && request.getSession().getAttribute("adminStatus") != null){ %>
    <% if((Boolean)request.getSession().getAttribute("adminStatus").equals(true)){ %>
      <a href="/admin">Admin</a>
    <% }} %>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>About IMhere!</h1>
      <p>A chat app for professionals.</p>
      </br>
      <p>
        Members of The Java Llamas:
      </p>

      <ul>
        <li><strong>Sachin Joglekar</strong> Project Advisor</li>
        <li><strong>Alisha Nanda</strong> UC Davis</li>
        <li><strong>Rebecca Ruvalcaba</strong> Parsons School of Design</li>
        <li><strong>Jordan Tyner</strong> UCLA</li>
      </ul>

    </div>
  </div>
</body>
</html>
