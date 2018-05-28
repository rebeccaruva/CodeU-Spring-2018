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
import codeu.model.data.Activity;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ActivityFeedServletTest {

  private ActivityFeedServlet activityServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ActivityStore mockActivityStore;

  @Before
  public void setup() {
    activityServlet = new ActivityFeedServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activity-feed.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockActivityStore = Mockito.mock(ActivityStore.class);
    activityServlet.setActivityStore(mockActivityStore);
  }

  /** test doGet method of ActivityFeedServlet class creates mock activities list and checks request */
  @Test
  public void testDoGet() throws IOException, ServletException {
    List<Activity> fakeActivitiesList = new ArrayList<>();
    fakeActivitiesList.add(
        new Activity(Activity.Type.LOGGED_IN, UUID.randomUUID(), Instant.now(), UUID.randomUUID()));
    Mockito.when(mockActivityStore.getAllActivities()).thenReturn(fakeActivitiesList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("all_activities", fakeActivitiesList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  /** test that doPost redirects to /activity-feed page */
  @Test
  public void testDoPost() throws IOException, ServletException {
    activityServlet.doPost(mockRequest, mockResponse);
    Mockito.verify(mockResponse).sendRedirect("/activity-feed");
  }
}
