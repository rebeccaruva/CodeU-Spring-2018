package codeu.controller;

import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.data.User;
import codeu.model.data.Conversation;
import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*Servlet class responsible for Admin page functions */
public class AdminServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * This function fires when a user requests the /admin URL. It simply forwards the request to
   * admin.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Conversation> conversations = conversationStore.getAllConversations();
    request.setAttribute("conversations", conversations);
    request.setAttribute("numusers", userStore.numUsers());
    request.setAttribute("numMessages", messageStore.numberOfMessages());
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form to give another username admin status.. It
   * gets the username from the submitted form data, checks for validity and if correct, gives admin
   * status to the user.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String adminUsername = request.getParameter("adminUsername");

    if (!userStore.isUserRegistered(adminUsername)) {
      request.setAttribute("error", "That username was not found.");
      doGet(request, response);
      return;
    }

    User user = userStore.getUser(adminUsername);

    if (user.getAdmin()) {
      request.setAttribute("error", "This user is already an admin.");
      doGet(request, response);
      return;
    }

    user.giveAdmin();
    request.setAttribute("success", adminUsername + " is now an admin.");
    doGet(request, response);
  }
}
