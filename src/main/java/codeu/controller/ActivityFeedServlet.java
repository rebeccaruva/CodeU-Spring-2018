// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.util.*;
import java.lang.*;
import java.io.*;

/** servlet responsible for activity feed */
public class ActivityFeedServlet extends HttpServlet{

  private ConversationStore conversationStore;

  /* initialize global variables */
  @Override
  public void init() throws ServletException{
    super.init();
    setConversationStore(ConversationStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * This function fires when a user navigates to the activity page. It forwards to activity-feed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Conversation> conversations = new ArrayList<Conversation>();
    conversations.addAll(conversationStore.getAllConversations()); // add all conversations to empty list
    if(conversations != null && !conversations.isEmpty()) Collections.sort(conversations, new sortConversations());
    request.setAttribute("sorted_conversations", conversations); // add sorted list of conversations to request
    request.getRequestDispatcher("/WEB-INF/view/activity-feed.jsp").forward(request, response);
  }

  class sortConversations implements Comparator<Conversation>{
    /* compare method for conversations in list - return in reverse order by time */
    public int compare(Conversation conversation1, Conversation conversation2){
      return -1 * (((conversation1.getCreationTime()).toString()).compareTo(((conversation2.getCreationTime()).toString())));
    }
  }

  /**
   * This function fires when a user submits the form on the activty feed page. It
   * redirects back to the activity feed page. Not used yet because no forms or data submitted.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // redirect to a GET request to activity-feed
    response.sendRedirect("/activity-feed");
  }
}
