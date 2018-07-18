package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*Servlet class responsible for Notification class functions */
public class NotificationServlet extends HttpServlet {

  /**
   * This function fires when a user requests the /notifications URL. It simply forwards the request
   * to notifications.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/notifications.jsp").forward(request, response);
  }
}
