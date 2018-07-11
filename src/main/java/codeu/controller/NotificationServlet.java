package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.NotificationStore;
import codeu.model.store.basic.UserStore;
import java.util.ArrayList;
import java.util.List;

/*Servlet class responsible for Notification class functions */
public class NotificationServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Notifications. */
  private NotificationStore notificationStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setNotificationStore(NotificationStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the NotificationStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setNotificationStore(NotificationStore notificationStore) {
    this.notificationStore = notificationStore;
  }

  /**
   * This function fires when a user requests the /notifications URL. It simply forwards the request
   * to notifications.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    NotificationServlet.updateNumNotifications(request);
    if (request.getSession().getAttribute("user") != null) {
      String username = (String) request.getSession().getAttribute("user");
      User user = UserStore.getInstance().getUser(username);
      request.setAttribute("notifications", notificationStore.getNotificationsForUser(user));
    }
    request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
  }

  /**
   * This function updates request with the current number of unread notifications for the logged in user.
   */
  public static void updateNumNotifications(HttpServletRequest request) {
    if (request.getSession().getAttribute("user") != null) {
      String username = (String) request.getSession().getAttribute("user");
      User user = UserStore.getInstance().getUser(username);
      request.getSession().setAttribute("numNotifications", NotificationStore.getInstance().getNumNotificationsForUser(user));
      request.getSession().setAttribute("numUnreadNotifications", NotificationStore.getInstance().getNumUnreadNotificationsForUser(user));
    }
    else {
      request.getSession().setAttribute("numNotifications", (int)0);
      request.getSession().setAttribute("numUnreadNotifications", (int)0);
    }
  }
}
