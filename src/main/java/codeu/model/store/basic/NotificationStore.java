package codeu.model.store.basic;

import codeu.model.data.Notification;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
 public class NotificationStore {

   /** Singleton instance of NotificationStore. */
   private static NotificationStore instance;

   /**
   * Return the singleton instance of NotificationStore that should be shared between all Servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
   public static NotificationStore getInstance() {
     if (instance == null) {
       instance = new NotificationStore(PersistentStorageAgent.getInstance());
     }
     return instance;
   }

   /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
   public static NotificationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
     return new NotificationStore(persistentStorageAgent);
   }

   /**
   * The PersistentStorageAgent responsible for loading Notifications from and saving
   * Notifications to DataStore.
   */
   private PersistentStorageAgent persistentStorageAgent;

   /** The in-memory list of Notifications. */
   private List<Notification> notifications;

   /** This class is a singleton, so its constructor is private. Call getInstance() insead */
   private NotificationStore(PersistentStorageAgent persistentStorageAgent) {
     this.persistentStorageAgent = persistentStorageAgent;
     notifications = new ArrayList<>();
   }

   /** Add a new Notification to the current set of Notifiations known to the application. */
   public void addNotification(Notification notification) {
     notifications.add(notification);
     persistentStorageAgent.writeThrough(notification);
   }

   /** Deletes a Notification from the current set and the DataStore */
   public void deleteNotification(Notification notification){
     notifications.remove(notification);
     persistentStorageAgent.deleteEntity(notification);
   }

   /** Access the current set of Notifications for a specific User */
   public List<Notification> getNotificationsForUser(User user) {
     List<Notification> notificationsForUser = new ArrayList<>();
     UUID userUUID = user.getId();
     for (Notification notification : notifications) {
       if (notification.getNotifiedUserUUID().equals(userUUID))
        notificationsForUser.add(notification);
     }

     return notificationsForUser;
   }

   /** Return the number of notifications a user has. */
   public int getNumNotificationsForUser(User user) {
     return getNotificationsForUser(user).size();
   }

   /** Sets the List of Notifications stored by this MessageStore. */
   public void setNotifications(List<Notification> notifications) {
     this.notifications = notifications;
   }
 }
