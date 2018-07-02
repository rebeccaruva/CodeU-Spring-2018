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
Resources used to help with styling tooltip and modal!
  tooltip/hover popup: https://www.w3schools.com/howto/howto_css_tooltip.asp
  modal: https://www.w3schools.com/howto/howto_css_modals.asp
--%>

<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <!-- Add icon library for buttons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };

    // When the user clicks the button, open the modal
    function showModal() {
      var modal = document.getElementById('myModal');
      modal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    function closeModal() {
      var modal = document.getElementById('myModal');
      modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      var modal = document.getElementById('myModal');
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }
  </script>
</head>
<body onload="scrollChat()">

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

    <%-- arrow button to view shared photos page --%>
    <div id="chatHead">
      <div id="chatTitle">
        <h1><%= conversation.getTitle() %></h1>
      </div>
      <div id="chatButton1">
        <a href="/photos/<%= conversation.getTitle() %>" class="butn" id="arrowb"><i class="fa fa-chevron-right fa-lg"></i></a>
        <span class="tooltiptext">Shared Photos</span>
      </div>
      <div id="chatButton2">
        <a href="">&#8635;</a>
      </div>
    </div>

    <hr/>

    <div id="chat">
      <ul>
    <%
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
    %>
      <li><strong><%= author %>:</strong> <%= message.getContent() %></li>
    <%
      }
    %>
      </ul>
    </div>

    <hr/>

    <div id="messageWrapper">
      <div id="first">
         <% if (request.getSession().getAttribute("user") != null) { %>
          <form action="/chat/<%= conversation.getTitle() %>" method="POST">
              <input type="text" name="message">
              <br/>
              <button type ="button">Check Sentiment</button>
              <button type="submit">Send</button>
          </form>
        </div>
        <div id="second">
          <%-- button icon for triggering modal to appear --%>
          <button class="butn" id="camerab" onclick="showModal()"><i class="fa fa-camera fa-lg"></i></button>
        </div>
      </div>

      <!-- the modal for photo upload -->
      <div id="myModal" class="modal">

        <!-- modal content -->
        <div class="modal-content">
          <span class="close" onclick="closeModal()">&times;</span>
          <p>Upload your photo here.</p>
        </div>
      </div>
      <% } else { %>
        <p><a href="/login">Login</a> to send a message.</p>
      <% } %>

    <hr/>

  </div>

</body>
</html>
