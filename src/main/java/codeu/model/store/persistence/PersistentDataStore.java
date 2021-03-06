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

package codeu.model.store.persistence;

import codeu.LanguageDictionary;
import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Notification;
import codeu.model.store.basic.NotificationStore;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String password = (String) entity.getProperty("password_hash");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String preferred_language = (String) entity.getProperty("preferred_language");
        Boolean adminStatus = (Boolean) entity.getProperty("adminStatus");
        User user = new User(uuid, userName, password, creationTime, adminStatus);
        user.setLanguage(preferred_language);
        users.add(user);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List, sorted in
   * ascending order by creation time.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      HashMap<String, String> translations = new HashMap<String, String>();
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        Message message =
            new Message(uuid, conversationUuid, authorUuid, content, creationTime);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }

  /**
   * Loads Message object from the Datastore service with language.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
  public String loadSpecificLanguage(UUID id, String language) {
    // Retrieve message with id from datastore
    Query query = new Query("chat-messages").setFilter(new FilterPredicate("uuid", FilterOperator.EQUAL, id.toString()));
    Entity messageEntity = (datastore.prepare(query)).asSingleEntity();

    try {
       // get and return translated text
      String property = "message-" + language;
      String translatedText = (String) messageEntity.getProperty(property);

      return translatedText;
    } catch (Exception e) {}
    return null;
  }

  /**
   * Loads all Activity objects from the Datastore service and returns them in a List, sorted in
   * descending order by creation time.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
  public List<Activity> loadActivities() throws PersistentDataStoreException {

    List<Activity> activities = new ArrayList<>();

    // Retrieve all activities from the datastore.
    Query query = new Query("chat-activities").addSort("creation_time", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID objectId = UUID.fromString((String) entity.getProperty("objectId"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        Activity.Type type = Activity.Type.valueOf((String) (entity.getProperty("type")));
        activities.add(new Activity(type, objectId, creationTime, uuid));
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return activities;
  }

  /**
   * Loads all Notification objects from the Datastore service and returns them in a List, sorted in
   * descending order by creation time.
   *
   * @throws codeu.model.store.persistence.PersistentDataStoreException if an error was detected
   *     during the load from the Datastore service
   */
   public List<Notification> loadNotifications() throws PersistentDataStoreException {

     List<Notification> notifications = new ArrayList<>();

     // Retrieve all notificatins from the datastore.
     Query query = new Query("notifications").addSort("creation_time", SortDirection.DESCENDING);
     PreparedQuery results = datastore.prepare(query);

     for (Entity entity : results.asIterable()) {
       try {
         UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
         UUID notifiedUser_UUID = UUID.fromString((String) entity.getProperty("notifiedUser_UUID"));
         UUID message_UUID = UUID.fromString((String) entity.getProperty("message_UUID"));
         Boolean viewedStatus = (boolean) entity.getProperty("viewedStatus");
         Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
         notifications.add(new Notification(uuid, notifiedUser_UUID, message_UUID, creationTime));
         if (viewedStatus == true) {
           NotificationStore.getInstance().markNotificationAsViewed(notifications.get(notifications.size()-1));
         }
       } catch (Exception e) {
         // In a production environment, errors should be very rare. Errors which may
         // occur include network errors, Datastore service errors, authorization errors,
         // database entity definition mismatches, or service mismatches.
         throw new PersistentDataStoreException(e);
       }
     }

     return notifications;
   }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users", user.getId().toString());
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password_hash", user.getPasswordHash());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("preferred_language", user.getLanguagePreference().toString());
    userEntity.setProperty("adminStatus", user.getAdminStatus());
    datastore.put(userEntity);
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages", message.getId().toString());
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    HashMap<String, String> translations = (new LanguageDictionary()).getDict();
    String property = "";
    // adds columns for each language and puts values from map into columns where exist
    for (String key : translations.keySet()) {
      property = "message-" + key;
      if (key.equals("en"))
        messageEntity.setProperty(property, message.getContent());
      else {
        messageEntity.setProperty(property, null);
      }
    }
    datastore.put(messageEntity);
  }

  /** Update Message object in Datastore with text in language */
  public void updateMessage(UUID id, String translatedText, String language) {
    try{
      // retrieve Message with id
      Query query = new Query("chat-messages").setFilter(new FilterPredicate("uuid", FilterOperator.EQUAL, id.toString()));
      Entity messageEntity = (datastore.prepare(query)).asSingleEntity();

      // update datastore with text in language
      String property = "message-" + language;
      messageEntity.setProperty(property, translatedText);

      datastore.put(messageEntity);
    } catch(Exception e){}
  }

  /** Update User object with language preference */
  public void updateUser(User user) {
    try{
      // retrieve User from datastore
      Query query = new Query("chat-users").setFilter(new FilterPredicate("uuid", FilterOperator.EQUAL, user.getId().toString()));
      Entity userEntity = (datastore.prepare(query)).asSingleEntity();

      // update User with language preference and put in datastore 
      userEntity.setProperty("preferred_language", user.getLanguagePreference().toString());
      datastore.put(userEntity);
    } catch(Exception e){}
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations", conversation.getId().toString());
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
    datastore.put(conversationEntity);
  }

  /** Write an Activity object to the Datastore service. */
  public void writeThrough(Activity activity) {
    Entity activityEntity = new Entity("chat-activities", activity.getId().toString());
    activityEntity.setProperty("uuid", activity.getId().toString());
    activityEntity.setProperty("objectId", activity.getObjectId().toString());
    activityEntity.setProperty("type", activity.getType().toString());
    activityEntity.setProperty("creation_time", activity.getCreationTime().toString());
    datastore.put(activityEntity);
  }

  /** Write a Notification object to the Datastore service. */
  public void writeThrough(Notification notification) {
    Entity notificationEntity = new Entity("notifications", notification.getId().toString());
    notificationEntity.setProperty("uuid", notification.getId().toString());
    notificationEntity.setProperty("notifiedUser_UUID", notification.getNotifiedUserUUID().toString());
    notificationEntity.setProperty("message_UUID", notification.getMessageUUID().toString());
    notificationEntity.setProperty("viewedStatus", notification.getViewedStatus());
    notificationEntity.setProperty("creation_time", notification.getCreationTime().toString());
    datastore.put(notificationEntity);
  }

  /** Updates a Notification object on the Datastore service. */
  public void updateEntity(Notification notification) {
    try{
      Query query =
        new Query("notifications").setFilter(new FilterPredicate("uuid", FilterOperator.EQUAL, notification.getId().toString()));
      PreparedQuery pq = datastore.prepare(query);
      Entity result = pq.asSingleEntity();
      result.setProperty("viewedStatus", notification.getViewedStatus());
      datastore.put(result);
    } catch (Exception e){}
  }

  /** Deletes a Notification object from the DataStore service. */
  public void deleteEntity(Notification notification) {
    Key k1 = KeyFactory.createKey("notifications", notification.getId().toString());
    datastore.delete(k1);
  }
}
