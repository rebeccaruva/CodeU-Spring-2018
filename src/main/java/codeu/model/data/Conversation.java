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

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation extends Activity{
  public final UUID owner;
  public final String title;
  private final UUID id;
  private final Instant creation;
  private int type;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation) {
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
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

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  public String getUser(){
    UserStore userStore = UserStore.getInstance();
    return userStore.getUser(owner).getName();
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  public String getConversation(){
    return null;
  }

}
