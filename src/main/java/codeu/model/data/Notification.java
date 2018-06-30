package codeu.model.data;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;
import java.time.Instant;
import java.util.UUID;

/** Class representing a notification. */
public class Notification {

  private final UUID id;
  private final UUID notifiedUser_UUID;
  private final UUID message_UUID;
  private Boolean viewedStatus;
  private final Instant creation;

  /** Constructs a new Notification.
   *
   * @param id the ID of this Notification
   * @param notifiedUser the user this Notification is for
   * @param message the message this Notification is about
   * @param viewedStatus whether or not this Notification has been seen
   * @param creation the creation time of this Notification
   */
   public Notification(UUID id, UUID notifiedUser_UUID, UUID message_UUID, Instant creation) {
     this.id = id;
     this.notifiedUser_UUID = notifiedUser_UUID;
     this.message_UUID = message_UUID;
     this.viewedStatus = false;
     this.creation = creation;
   }

   /** Returns the ID of this Notification */
   public UUID getId() {
     return id;
   }

   /** Returns the User this Notification is for */
   public User getNotifiedUser() {
     UserStore userStore = UserStore.getInstance();
     return userStore.getUser(notifiedUser_UUID);
   }

   /** Returns the UUID of the User this Notification is for */
   public UUID getNotifiedUserUUID() {
     return notifiedUser_UUID;
   }

   /** Returns the Message this Notification refers to */
   public Message getMessage() {
     MessageStore messageStore = MessageStore.getInstance();
     return messageStore.getMessage(message_UUID);
   }

   /** Returns the UUID of the Message this Notification refers to. This is necessary
     * because the Notification is constructed before the Message is.
     */
   public UUID getMessageUUID() {
     return message_UUID;
   }

   /** Returns whether or not this Notification has been viewed by notifiedUser */
   public Boolean getViewedStatus() {
     return viewedStatus;
   }

   /** Returns the creation time of this Notification */
   public Instant getCreationTime() {
     return creation;
   }

}
