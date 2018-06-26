package codeu.model.data;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import java.time.Instant;
import java.util.UUID;

/** Class representing a notification. */
public class Notification {

  private final UUID id;
  private final User notifiedUser;
  private final Message message;
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
   public Notification(UUID id, User notifiedUser, Message message, Instant creation) {
     this.id = id;
     this.notifiedUser = notifiedUser;
     this.message = message;
     this.viewedStatus = false;
     this.creation = creation;
   }

   /** Returns the ID of this Notification */
   public UUID getID() {
     return id;
   }

   /** Returns the User this Notification is for */
   public User getNotifiedUser() {
     return notifiedUser;
   }

   /** Returns the Message this Notification refers to */
   public Message getMessage() {
     return message;
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
