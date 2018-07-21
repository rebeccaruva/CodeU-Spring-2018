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

<%--
Resource used to help with styling grid!
  https://www.w3schools.com/howto/howto_js_image_grid.asp
--%>

<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %> Shared Photos</title>
  <link rel="stylesheet" href="/css/main.css">
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
  <link rel="icon" href="/favicon.ico" type="image/x-icon">
  <!-- Add icon library for button -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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

      <div id="photoHead">
        <div id="photoTitle">
          <h1><%= conversation.getTitle() %> Shared Photos</h1>
        </div>
        <div id="photoBackArrow">
          <a href="/chat/<%= conversation.getTitle() %>" class="butn" id="arrowb"><i class="fa fa-chevron-left fa-lg"></i></a>
        </div>
      </div>

      <hr/>

      <% if (request.getSession().getAttribute("user") == null){ %>
        <p>Please <a href="/login">login</a> to view shared photos.</p>
      <% } else { %>

      <!-- photo grid with example photos -->
      <div class="row">
        <div class="column">
          <img src="https://images.pexels.com/photos/356378/pexels-photo-356378.jpeg?cs=srgb&dl=adorable-animal-breed-356378.jpg&fm=jpg" style="width:100%">
          <img src="https://images.pexels.com/photos/356378/pexels-photo-356378.jpeg?cs=srgb&dl=adorable-animal-breed-356378.jpg&fm=jpg" style="width:100%">
          <img src="https://images.pexels.com/photos/356378/pexels-photo-356378.jpeg?cs=srgb&dl=adorable-animal-breed-356378.jpg&fm=jpg" style="width:100%">
        </div>
        <div class="column">
          <img src="https://images.pexels.com/photos/39317/chihuahua-dog-puppy-cute-39317.jpeg?cs=srgb&dl=animal-chihuahua-cute-39317.jpg&fm=jpg" style="width:100%">
          <img src="https://images.pexels.com/photos/39317/chihuahua-dog-puppy-cute-39317.jpeg?cs=srgb&dl=animal-chihuahua-cute-39317.jpg&fm=jpg" style="width:100%">
          <img src="https://images.pexels.com/photos/39317/chihuahua-dog-puppy-cute-39317.jpeg?cs=srgb&dl=animal-chihuahua-cute-39317.jpg&fm=jpg" style="width:100%">
        </div>
        <div class="column">
          <img src="https://images.pexels.com/photos/460823/pexels-photo-460823.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" style="width:100%">
          <img src="https://images.pexels.com/photos/460823/pexels-photo-460823.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" style="width:100%">
        </div>
        <div class="column">
          <img src="https://images.pexels.com/photos/825947/pexels-photo-825947.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" style="width:100%">
          <img src="https://images.pexels.com/photos/825947/pexels-photo-825947.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" style="width:100%">
          <img src="https://images.pexels.com/photos/825947/pexels-photo-825947.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" style="width:100%">
        </div>
      </div>
      <% } %>
    </div>
  </div>
</body>
</html>
