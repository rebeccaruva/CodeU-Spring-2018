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

package codeu.model.data;

import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.text.*;
import java.time.*;
import java.util.*;

/** Class representing an Activity, which includes creating users, conversations, and messages. */
public class Activity {
  // type of Activity
  public enum Type {
    REGISTERED,
    LOGGED_IN,
    NEW_CONVERSATION,
    NEW_MESSAGE
  };

  private final UUID objectId;
  private final Type type;
  private final Instant creation;
  private final UUID id;

  /**
   * Constructs a new Activity.
   *
   * @param objectId the ID of the object that handles this Activity
   * @param type the type of this Activity
   * @param creation the time this Activity was created
   * @param id the ID of this Activity
   */
  public Activity(Type type, UUID objectId, Instant creationTime, UUID id) {
    this.type = type;
    this.objectId = objectId;
    this.creation = creationTime;
    this.id = id;
  }

  /** Returns type of Activity. */
  public Type getType() {
    return type;
  }

  /** Returns id of Activity */
  public UUID getId() {
    return id;
  }

  /** Returns id of object that handles this Activity. */
  public UUID getObjectId() {
    return objectId;
  }

  /** Returns time Activity was created. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Formats Instant variable as date for clean output. */
  public String formattedTime() {
    DateFormat pstFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    pstFormat.setTimeZone(TimeZone.getTimeZone("PST"));

    return (pstFormat.format(Date.from(creation)).toString() + " PST");
  }

  /** Gets User associated with Activity. */
  public User getUser() {
    return (UserStore.getInstance()).getUser(objectId);
  }

  /** Gets Conversation associated with Activity. */
  public Conversation getConversation() {
    return (ConversationStore.getInstance()).getConversation(objectId);
  }

  /** Gets Message associated with Activity. */
  public Message getMessage() {
    return (MessageStore.getInstance()).getMessage(objectId);
  }
}
