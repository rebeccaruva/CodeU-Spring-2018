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
  <title>Register</title>
  <link rel="stylesheet" href="/css/main.css">
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
  <link rel="icon" href="/favicon.ico" type="image/x-icon">
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

  <div id="container">
    <h1>Register</h1>

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/register" method="POST">
      <label for="username">Username: </label>
      <br/>
      <input type="text" name="username" id="username">
      <br/>
      <label for="password">Password: </label>
      <br/>
      <input type="password" name="password" id="password">
      <br/><br/>
      <button type="submit">Submit</button>
    </form>
  </div>
</body>
</html>
