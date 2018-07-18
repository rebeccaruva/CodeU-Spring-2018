package codeu.controller;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.NotificationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.persistence.PersistentDataStoreException;
import codeu.model.store.persistence.PersistentStorageAgent;
import codeu.configuration.JsonConfiguration;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener class that fires when the server first starts up, before any servlet classes are
 * instantiated.
 */
public class ServerStartupListener implements ServletContextListener {

  /** Loads data from Datastore. */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      List<User> users = PersistentStorageAgent.getInstance().loadUsers();
      UserStore.getInstance().setUsers(users);

      List<Conversation> conversations = PersistentStorageAgent.getInstance().loadConversations();
      ConversationStore.getInstance().setConversations(conversations);

      List<Message> messages = PersistentStorageAgent.getInstance().loadMessages();
      MessageStore.getInstance().setMessages(messages);

      List<Activity> activities = PersistentStorageAgent.getInstance().loadActivities();
      ActivityStore.getInstance().setActivities(activities);

      /* only uncomment next line if do not run makeJson test in NaturalLanguageProcessingTest for
        JsonConfiguration, i.e. run maven with option -DskipTests=true
      */
      //(new JsonConfiguration()).createJson();

      List<Notification> notifications = PersistentStorageAgent.getInstance().loadNotifications();
      NotificationStore.getInstance().setNotifications(notifications);
    } catch (PersistentDataStoreException e) {
      System.err.println("Server didn't start correctly. An error occurred during Datastore load!");
      System.err.println("This is usually caused by loading data that's in an invalid format.");
      System.err.println("Check the stack trace to see exactly what went wrong.");
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}
