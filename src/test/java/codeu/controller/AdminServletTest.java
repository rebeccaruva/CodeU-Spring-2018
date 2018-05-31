package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
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

public class AdminServletTest {

  private AdminServlet adminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
  private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    adminServlet = new AdminServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);

    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    adminServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    adminServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    adminServlet.setUserStore(mockUserStore);
  }

  /** test that doGet calculates statistics from the respective dataStores correctly */
  @Test
  public void testDoGet() throws IOException, ServletException {
    Mockito.when(mockConversationStore.numConversations())
        .thenReturn(3);

    Mockito.when(mockUserStore.numUsers())
        .thenReturn(4);

    Mockito.when(mockMessageStore.numberOfMessages())
        .thenReturn(43);

    adminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("numConversations", 3);
    Mockito.verify(mockRequest).setAttribute("numUsers", 4);
    Mockito.verify(mockRequest).setAttribute("numMessages", 43);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  /** test that users do not have admin status by default, and can have it added */
  @Test
  public void testAdminStatus() throws IOException, ServletException {
    UUID id = UUID.randomUUID();
    String name = "test_username";
    String passwordHash = "$2a$10$bBiLUAVmUFK6Iwg5rmpBUOIBW6rIMhU1eKfi3KR60V9UXaYTwPfHy";
    Instant creation = Instant.now();
    Boolean adminStatus = false;
    User user = new User(id, name, passwordHash, creation, adminStatus);

    Assert.assertEquals(adminStatus, user.getAdminStatus());
    user.giveUserAdminStatus();
    Assert.assertEquals(true, user.getAdminStatus());
  }

}
