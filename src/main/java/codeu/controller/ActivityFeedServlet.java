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

import codeu.model.data.Activity;
import codeu.model.store.basic.ActivityStore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.controller.NotificationServlet;

/** servlet responsible for activity feed */
public class ActivityFeedServlet extends HttpServlet {

  /** Store class that gives access to Activities */
  private ActivityStore activityStore;

  /** initialize global variables */
  @Override
  public void init() throws ServletException {
    super.init();
    setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the ActivityStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /**
   * This function fires when a user navigates to the activity page. It adds list of activities to
   * request then forwards to activity-feed.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Activity> activities = activityStore.getAllActivities();
    request.setAttribute("all_activities", activities);
    NotificationServlet.updateNumNotifications(request);
    request.getRequestDispatcher("/WEB-INF/view/activity-feed.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the activty feed page. It redirects back to
   * the activity feed page. Not used yet because no forms or data submitted.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // redirect to a GET request to activity-feed
    response.sendRedirect("/activity-feed");
  }
}
