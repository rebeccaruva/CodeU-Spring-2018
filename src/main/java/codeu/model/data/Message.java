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

import java.time.Instant;
import java.util.UUID;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;

/** Class representing a message. Messages are sent by a User in a Conversation. */
public class Message extends Activity{

  private final UUID conversation;
  private final UUID author;
  private final String content;
  private final UUID id;
  private final Instant creation;
  private int type;

  /**
   * Constructs a new Message.
   *
   * @param id the ID of this Message
   * @param conversation the ID of the Conversation this Message belongs to
   * @param author the ID of the User who sent this Message
   * @param content the text content of this Message
   * @param creation the creation time of this Message
   */
  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.content = content;
    this.creation = creation;
  }

  public void setType(int type){
    this.type = type;
  }

  public int getType(){
    return type;
  }

  /** Returns the ID of this Activity. */
  public UUID getId() {
    return id;
  }

  /** Returns the creation time of this Activity. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the ID of the Conversation this Message belongs to. */
  public UUID getConversationId() {
    return conversation;
  }

  /** Returns the ID of the User who sent this Message. */
  public UUID getAuthorId() {
    return author;
  }

  /** Returns the text content of this Message. */
  public String getContent() {
    return content;
  }

  public String getTitle(){
    return this.getContent();
  }

  public String getUser(){
    UserStore userStore = UserStore.getInstance();
    return userStore.getUser(author).getName();
  }

  public String getConversation(){
    ConversationStore conversationStore = ConversationStore.getInstance();
    return conversationStore.getConversation(conversation).getTitle();
  }

}
