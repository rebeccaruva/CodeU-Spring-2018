package codeu.model.store.basic;

import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NotificationStoreTest {

  private NotificationStore notificationStore;
  private PersistentStorageAgent mockPersistentStorageAgent;

  private final UUID USER_ID_ONE = UUID.randomUUID();
  private final UUID USER_ID_TWO = UUID.randomUUID();
  private final UUID USER_ID_THREE = UUID.randomUUID();

  private final User USER_ONE =
      new User(
          USER_ID_ONE,
          "test_username_one",
          "$2a$10$/zf4WlT2Z6tB5sULB9Wec.QQdawmF0f1SbqBw5EeJg5uoVpKFFXAa",
          Instant.ofEpochMilli(1000),
          false);
  private final User USER_TWO =
      new User(
          USER_ID_TWO,
          "test_username_two",
          "$2a$10$lgZSbmcYyyC7bETcMo/O1uUltWYDK3DW1lrEjCumOE1u8QPMlzNVy",
          Instant.ofEpochMilli(2000),
          false);
  private final User USER_THREE =
      new User(
          USER_ID_THREE,
          "test_username_three",
          "$2a$10$lgZSbmcYyyC7bETcMo/O1uUltWYDK3DW1lrEjCumOE1u8QPMlzNVy",
          Instant.ofEpochMilli(2000),
          false);
  private final Notification NOTIFICATION_ONE =
      new Notification(
          UUID.randomUUID(),
          USER_ID_ONE,
          UUID.randomUUID(),
          Instant.ofEpochMilli(1000));
  private final Notification NOTIFICATION_TWO =
      new Notification(
          UUID.randomUUID(),
          USER_ID_ONE,
          UUID.randomUUID(),
          Instant.ofEpochMilli(1000));
  private final Notification NOTIFICATION_THREE =
      new Notification(
            UUID.randomUUID(),
            USER_ID_TWO,
            UUID.randomUUID(),
            Instant.ofEpochMilli(1000));

  @Before
  public void setup() {
    mockPersistentStorageAgent = Mockito.mock(PersistentStorageAgent.class);
    notificationStore = NotificationStore.getTestInstance(mockPersistentStorageAgent);

    final List<Notification> notificationList = new ArrayList<>();
    notificationList.add(NOTIFICATION_ONE);
    notificationList.add(NOTIFICATION_TWO);
    notificationList.add(NOTIFICATION_THREE);
    notificationStore.setNotifications(notificationList);
  }

  @Test
  public void testAddNotification() {
    Notification inputNotification =
        new Notification(
            UUID.randomUUID(),
            USER_ID_THREE,
            UUID.randomUUID(),
            Instant.now());

    notificationStore.addNotification(inputNotification);
    Notification resultNotification = notificationStore.getNotificationsForUser(USER_THREE).get(0);

    assertEquals(inputNotification, resultNotification);
    Mockito.verify(mockPersistentStorageAgent).writeThrough(inputNotification);
  }

  @Test
  public void testGetNotificationsForUser() {
    List<Notification> resultNotifications = notificationStore.getNotificationsForUser(USER_ONE);
    Assert.assertEquals(2, resultNotifications.size());
    assertEquals(NOTIFICATION_ONE, resultNotifications.get(0));
    assertEquals(NOTIFICATION_TWO, resultNotifications.get(1));

    resultNotifications = notificationStore.getNotificationsForUser(USER_TWO);
    assertEquals(NOTIFICATION_THREE, resultNotifications.get(0));
  }

  @Test
  public void testGetUnreadNotificationsForUser() {
    notificationStore.markNotificationAsViewed(NOTIFICATION_ONE);
    List<Notification> resultNotifications = notificationStore.getUnreadNotificationsForUser(USER_ONE);
    Assert.assertEquals(1, resultNotifications.size());
    assertEquals(NOTIFICATION_TWO, resultNotifications.get(0));

    resultNotifications = notificationStore.getNotificationsForUser(USER_TWO);
    assertEquals(NOTIFICATION_THREE, resultNotifications.get(0));
  }

  @Test
  public void testGetReadNotificationsForUser() {
    notificationStore.markNotificationAsViewed(NOTIFICATION_ONE);
    List<Notification> resultNotifications = notificationStore.getReadNotificationsForUser(USER_ONE);
    Assert.assertEquals(1, resultNotifications.size());
    assertEquals(NOTIFICATION_ONE, resultNotifications.get(0));
  }

  @Test
  public void testGetNumNotificationsForUser() {
    List<Notification> resultNotifications = notificationStore.getNotificationsForUser(USER_ONE);
    Assert.assertEquals(2, resultNotifications.size());
  }

  @Test
  public void testGetNumUnreadNotificationsForUser() {
    List<Notification> resultNotifications = notificationStore.getNotificationsForUser(USER_ONE);
    resultNotifications.get(0).markAsViewed();
    int numUnreadNotifications = notificationStore.getNumUnreadNotificationsForUser(USER_ONE);
    Assert.assertEquals(notificationStore.getNumUnreadNotificationsForUser(USER_ONE), numUnreadNotifications);
  }

  @Test
  public void testGetNumReadNotificationsForUser() {
    notificationStore.markNotificationAsViewed(NOTIFICATION_ONE);
    List<Notification> resultNotifications = notificationStore.getReadNotificationsForUser(USER_ONE);
    Assert.assertEquals(notificationStore.getNumReadNotificationsForUser(USER_ONE), resultNotifications.size());
  }

  @Test
  public void testDeleteNotification() {
    final List<Notification> notificationList = new ArrayList<>();
    notificationList.add(NOTIFICATION_ONE);
    notificationList.add(NOTIFICATION_TWO);
    notificationStore.setNotifications(notificationList);
    notificationStore.deleteNotification(NOTIFICATION_ONE);

    List<Notification> resultNotifications = notificationStore.getNotificationsForUser(USER_ONE);
    Assert.assertEquals(1, resultNotifications.size());
    assertEquals(NOTIFICATION_TWO, resultNotifications.get(0));
  }

  @Test
  public void testDeleteAllNotificationsForUser() {
    Assert.assertEquals(2, notificationStore.getNotificationsForUser(USER_ONE).size());
    notificationStore.deleteAllNotificationsForUser(USER_ONE);
    Assert.assertEquals(0, notificationStore.getNotificationsForUser(USER_ONE).size());
  }

  private void assertEquals(Notification expectedNotification, Notification actualNotification) {
    Assert.assertEquals(expectedNotification.getId(), actualNotification.getId());
    Assert.assertEquals(expectedNotification.getNotifiedUserUUID(), actualNotification.getNotifiedUserUUID());
    Assert.assertEquals(expectedNotification.getMessageUUID(), actualNotification.getMessageUUID());
    Assert.assertEquals(expectedNotification.getCreationTime(), actualNotification.getCreationTime());
  }
}
