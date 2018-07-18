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
import codeu.model.store.basic.UserStore;
import codeu.natural_language.NaturalLanguageProcessing;
import java.io.*;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

/** Class representing a message. Messages are sent by a User in a Conversation. */
public class Message {

  private final UUID conversation;
  private final UUID author;
  private final UUID id;
  private final Instant creation;
  private String content;
  private HashMap<String, String> translations;
  private UserStore userStore;

  /**
   * Constructs a new Message (2 constructors).
   *
   * @param id the ID of this Message
   * @param conversation the ID of the Conversation this Message belongs to
   * @param author the ID of the User who sent this Message
   * @param content the text content of this Message
   * @param creation the creation time of this Message
   * @param translations the map that stores used translations of message
   */
  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation) {
    userStore = UserStore.getInstance();
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.creation = creation;
    this.content = content;
    // translate content to English if different language
    if ((userStore.getUser(author) != null)
        && (userStore.getUser(author)).getLanguagePreference() != "en") {
      try {
        content =
            (new NaturalLanguageProcessing())
                .translate(content, (userStore.getUser(author)).getLanguagePreference(), "en");
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
    this.content = content;
    translations = new HashMap<String, String>();
    translations.put("en", content); // put English message into map
  }

  public Message(
      UUID id,
      UUID conversation,
      UUID author,
      String content,
      HashMap<String, String> translations,
      Instant creation) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.content = content;
    this.creation = creation;
    this.translations = new HashMap<String, String>();
    (this.translations).putAll(translations); // transfer data to map
  }

  /** Adds translation to message in targetLanguage */
  public String addTranslation(String targetLanguage) {
    try {
      String translatedText =
          (new NaturalLanguageProcessing()).translate(content, "en", targetLanguage);
      translations.put(targetLanguage, translatedText); // put translated text into map
      return translatedText;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return null;
  }

  /** Gets translation of specific language */
  public String getTranslation(String language) {
    return translations.get(language);
  }

  /** Gets translation in language if in map or adds if not */
  public String getTranslationAndAdd(String language) {
    String translatedText = translations.get(language);
    if (translatedText != null) {
      return translatedText;
    }
    return addTranslation(language);
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

  /** Returns the text content of this Message, implements abstract method of Activity. */
  public String getTitle() {
    return this.getContent();
  }

  /** Returns the name of author of Message. */
  public String getUser() {
    UserStore userStore = UserStore.getInstance();
    return userStore.getUser(author).getName();
  }

  /** Returns the title of conversation this Message is under. */
  public String getConversation() {
    ConversationStore conversationStore = ConversationStore.getInstance();
    return conversationStore.getConversation(conversation).getTitle();
  }

  /** Returns the ID of this Message. */
  public UUID getId() {
    return id;
  }

  /** Returns the creation time of this Message. */
  public Instant getCreationTime() {
    return creation;
  }
}
