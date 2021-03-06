package codeu.model.store.persistence;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    String nameOne = "test_username_one";
    String passwordHashOne = "$2a$10$BNte6sC.qoL4AVjO3Rk8ouY6uFaMnsW8B9NjtHWaDNe8GlQRPRT1S";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Boolean adminOne = false;
    User inputUserOne = new User(idOne, nameOne, passwordHashOne, creationOne, adminOne);

    UUID idTwo = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String nameTwo = "test_username_two";
    String passwordHashTwo = "$2a$10$ttaMOMMGLKxBBuTN06VPvu.jVKif.IczxZcXfLcqEcFi1lq.sLb6i";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    boolean adminTwo = false;
    User inputUserTwo = new User(idTwo, nameTwo, passwordHashTwo, creationTwo, adminTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();

    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordHashOne, resultUserOne.getPasswordHash());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());
    Assert.assertEquals(adminOne, resultUserOne.getAdminStatus());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordHashTwo, resultUserTwo.getPasswordHash());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
    Assert.assertEquals(adminTwo, resultUserTwo.getAdminStatus());
  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID ownerOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne);

    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID ownerTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID conversationOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID authorOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    HashMap<String, String> mapOne = new HashMap<String, String>();
    mapOne.put("en", "Hello");
    mapOne.put("es", "Hola");
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, mapOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID conversationTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID authorTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    String contentTwo = "test content one";
    HashMap<String, String> mapTwo = new HashMap<String, String>();
    mapTwo.put("en", "How are you?");
    mapTwo.put("es", "Como estas?");
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, mapTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals("Hello", resultMessageOne.getTranslation("en"));
    Assert.assertEquals("Hola", resultMessageOne.getTranslation("es"));
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals("How are you?", resultMessageTwo.getTranslation("en"));
    Assert.assertEquals("Como estas?", resultMessageTwo.getTranslation("es"));
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadActivities() throws PersistentDataStoreException {
    Activity.Type typeOne = Activity.Type.LOGGED_IN;
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID objectIdOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    Instant creationOne = Instant.ofEpochMilli(1000);
    Activity inputActivityOne = new Activity(typeOne, objectIdOne, creationOne, idOne);

    Activity.Type typeTwo = Activity.Type.NEW_CONVERSATION;
    UUID idTwo = UUID.fromString("10000002-2222-3333-4444-555555555555");
    UUID objectIdTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    Instant creationTwo = Instant.ofEpochMilli(1000);
    Activity inputActivityTwo = new Activity(typeTwo, objectIdTwo, creationTwo, idTwo);

    // save
    persistentDataStore.writeThrough(inputActivityOne);
    persistentDataStore.writeThrough(inputActivityTwo);

    // load
    List<Activity> resultActivities = persistentDataStore.loadActivities();

    // confirm that what we saved matches what we loaded
    Activity resultActivityOne = resultActivities.get(0);
    Assert.assertEquals(idOne, resultActivityOne.getId());
    Assert.assertEquals(objectIdOne, resultActivityOne.getObjectId());
    Assert.assertEquals(creationOne, resultActivityOne.getCreationTime());

    Activity resultActivityTwo = resultActivities.get(1);
    Assert.assertEquals(idTwo, resultActivityTwo.getId());
    Assert.assertEquals(objectIdTwo, resultActivityTwo.getObjectId());
    Assert.assertEquals(creationTwo, resultActivityTwo.getCreationTime());
  }

  @Test
  public void testSaveAndLoadNotification() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID notifiedUserOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID messageOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    Instant creationOne = Instant.ofEpochMilli(1000);
    Notification inputNotificationOne =
        new Notification(idOne, notifiedUserOne, messageOne, creationOne);

    UUID idTwo = UUID.fromString("10000003-2222-3333-4444-555555555555");
    UUID notifiedUserTwo = UUID.fromString("10000004-2222-3333-4444-555555555555");
    UUID messageTwo = UUID.fromString("10000005-2222-3333-4444-555555555555");
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Notification inputNotificationTwo =
        new Notification(idTwo, notifiedUserTwo, messageTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputNotificationOne);
    persistentDataStore.writeThrough(inputNotificationTwo);

    // load
    List<Notification> resultNotifications = persistentDataStore.loadNotifications();

    // confirm that what we saved matches what we loaded
    // note that notifications are loaded in descending order
    Notification resultNotificationOne = resultNotifications.get(1);
    Assert.assertEquals(idOne, resultNotificationOne.getId());
    Assert.assertEquals(notifiedUserOne, resultNotificationOne.getNotifiedUserUUID());
    Assert.assertEquals(messageOne, resultNotificationOne.getMessageUUID());
    Assert.assertEquals(creationOne, resultNotificationOne.getCreationTime());
    Assert.assertEquals(false, resultNotificationOne.getViewedStatus());

    Notification resultNotificationTwo = resultNotifications.get(0);
    Assert.assertEquals(idTwo, resultNotificationTwo.getId());
    Assert.assertEquals(notifiedUserTwo, resultNotificationTwo.getNotifiedUserUUID());
    Assert.assertEquals(messageTwo, resultNotificationTwo.getMessageUUID());
    Assert.assertEquals(creationTwo, resultNotificationTwo.getCreationTime());
    Assert.assertEquals(false, resultNotificationTwo.getViewedStatus());
  }

  @Test
  public void testUpdateEntity() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID notifiedUserOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID messageOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    Instant creationOne = Instant.ofEpochMilli(1000);
    Notification inputNotificationOne =
        new Notification(idOne, notifiedUserOne, messageOne, creationOne);

    persistentDataStore.writeThrough(inputNotificationOne);
    inputNotificationOne.markAsViewed();
    persistentDataStore.updateEntity(inputNotificationOne);
    List<Notification> resultNotifications = persistentDataStore.loadNotifications();

    Notification resultNotificationOne = resultNotifications.get(0);
    Assert.assertEquals(true, resultNotificationOne.getViewedStatus());
  }

  @Test
  public void testDeleteEntity() throws PersistentDataStoreException {
    UUID idOne = UUID.fromString("10000000-2222-3333-4444-555555555555");
    UUID notifiedUserOne = UUID.fromString("10000001-2222-3333-4444-555555555555");
    UUID messageOne = UUID.fromString("10000002-2222-3333-4444-555555555555");
    Instant creationOne = Instant.ofEpochMilli(1000);
    Notification inputNotificationOne =
        new Notification(idOne, notifiedUserOne, messageOne, creationOne);

    persistentDataStore.writeThrough(inputNotificationOne);
    persistentDataStore.deleteEntity(inputNotificationOne);
    List<Notification> resultNotifications = persistentDataStore.loadNotifications();

    Assert.assertEquals(0, resultNotifications.size());

  }
}
