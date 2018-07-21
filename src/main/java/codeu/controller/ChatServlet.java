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

package codeu.controller;

import codeu.model.data.Activity;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.Notification;
import codeu.model.data.User;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.NotificationStore;
import codeu.model.store.basic.UserStore;
import codeu.natural_language.NaturalLanguageProcessing;
import com.vdurmont.emoji.EmojiParser; // import for emoji parser
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;
import java.util.regex.*;
import com.vladsch.flexmark.ast.Node; // imports for using markdown with flexmark
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import java.util.Arrays;
import com.vdurmont.emoji.EmojiParser; // import for emoji parser
//imports for image url checker
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import javax.imageio.stream.ImageInputStream;
import java.awt.Toolkit;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Activities. */
  private ActivityStore activityStore;

  /** Store class that gives access to Notifications. */
  private NotificationStore notificationStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
    setActivityStore(ActivityStore.getInstance());
    setNotificationStore(NotificationStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the ActivityStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /* this will return true if image url is valid */
	public static boolean isValid(String url) {
		//first try to create a valid URL
		try {
			new URL(url).toURI();
			return true;
		}

		// then if there was an Exception while creating URL
		catch (Exception e) {
			return false;
		}
	}

  /* this chunk of code should return true if image url is actual image
  * only works locally on devserver
  */
  // /* this will return true if image url is actual image */
  // public static boolean isImage(String imgURL) {
  //   boolean URLresult;
  //
  //   //read image
  //   try{
  //     URL url = new URL(imgURL);
  //     BufferedImage image = ImageIO.read(imageURL);
  //     System.out.println(image);
  //     if(image == null) {
  //       URLresult = false;
  //     } else {
  //       URLresult = true;
  //     }
  //   } catch (MalformedURLException e) {
  //     System.out.println("URL error with image" + e.getMessage());
  //     URLresult = false;
  //   } catch (IOException e) {
  //     System.out.println("IO error with image" + e.getMessage());
  //   }
  //   System.out.println(URLresult);
  //   return URLresult;
  // }

  /* this will return true if message is single emoji */
  public static boolean isOneEmoji(String emojiMess) {
    boolean emojiResult;

    // count all the colons(:) in the message
    int counter = 0;
    for(int i = 0; i < emojiMess.length(); i++) {
      if( emojiMess.charAt(i) == ':' ) {
        counter++;
      }
    }

    //if there are only 2 colons --> :example:, not ::example:
    //and the first and last index of (:) are the same length as String
    //then emoji is the only thing in message
    if(counter == 2) {
      if ((emojiMess.indexOf(':') == 0) && (emojiMess.lastIndexOf(':') == emojiMess.length()-1)){
        System.out.println("only one emoji");
        emojiResult = true;
      } else {
        System.out.println("emoji plus some extra characters");
        emojiResult = false;
      }
    } else {
      System.out.println("there are more than 2 colons (:)");
      emojiResult = false;
    }
    return emojiResult;
  }

  /* this will return modified message if contains photo */
  public String checkPhotoLink(String mess, int count) {
    String modifiedMessage = ""; // will use to return
    for (int i = 0; i < count; i++) {
      modifiedMessage = mess; //will return later

      int pl1 = mess.indexOf("photo link: "); //find the index of the photo link, should be 0
  		int pl2 = pl1 + 12; //find the concluding space index of the photo link

      //find image link and send to find if valid link or not
      int endOfLink = 0;
      String imageURL = "";
      if(mess.indexOf(" ", pl2) > 0) { //make sure end of link is not out of range
        endOfLink = mess.indexOf(" ", pl2);
        imageURL = mess.substring(pl2, endOfLink); //url in message until space
      } else {
        imageURL = mess.substring(pl2); //url in message until end
      }
      if (isValid(imageURL)) {
        // if(isImage(imageURL)) { //only works locally
          String modifiedImageURL = "<img src=\"" + imageURL + "\" style=\"max-width: 50%;\">";
          //isPhoto is true!!!

          //update message content with link and img src html
          //delete photo link and leave image link
          modifiedMessage = modifiedMessage.replace(imageURL, modifiedImageURL);
          modifiedMessage = modifiedMessage.replaceFirst("photo link: ", "");
          mess = modifiedMessage; //update
        // }
      }
    }

    //return message with modifications
    System.out.println(modifiedMessage);
    //get rid of any remaining photo link (ex. if link was not image)
    if (modifiedMessage.contains("photo link: ")){ //if still has photo link
      modifiedMessage = modifiedMessage.replaceAll("photo link: ", "");
    }
    return modifiedMessage;
  }


  /**
   * Sets the NotificationStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setNotificationStore(NotificationStore notificationStore) {
    this.notificationStore = notificationStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());
    String message = request.getParameter("message");

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    String username = (String) request.getSession().getAttribute("user");
    if (username != null) {
      User user = userStore.getUser(username);
      notificationStore.getInstance().markNotificationsForUserInConvoAsRead(user, conversation);
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    String english_msg = message;
    NaturalLanguageProcessing nlp = new NaturalLanguageProcessing();

    // translate message into English and check tone
    if (message != null) {
      if (user.getLanguagePreference() != "en")
        english_msg = nlp.translate(message, user.getLanguagePreference(), "en");
      request.setAttribute("score_result", nlp.checkTone(english_msg));
    }

    request.setAttribute("message", message);
    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    NotificationServlet.updateNumNotifications(request);
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }

    String messageText = request.getParameter("message");
    Pattern msgMentionPattern = Pattern.compile("\\[([a-zA-Z 0-9]+?)\\]");
    Matcher msgTxtMatcher = msgMentionPattern.matcher(messageText);

    // Generate a UUID for the message now, in case Notification needs it
    UUID messageID = UUID.randomUUID();

    List<String> mentionedUserStrings = new ArrayList<String>();
    while (msgTxtMatcher.find()){
      String mentionedUsername = msgTxtMatcher.group(1);
      User mentionedUser = userStore.getUser(mentionedUsername);
      if (mentionedUser != null && !mentionedUserStrings.contains(mentionedUsername)){
        // if a user was mentioned, place ** around the text so it will be made bold
        messageText = messageText.replace("["+mentionedUsername+"]","**["+mentionedUsername+"]**");
        // if a user isn't mentioning themselves, create a Notification for this mention
        if (mentionedUser.getId() != user.getId()){
          notificationStore.addNotification(new Notification(UUID.randomUUID(),mentionedUser.getId(),messageID,Instant.now()));
          mentionedUserStrings.add(mentionedUsername);
        }
      }
    }

    // this code uses flexmark library to parse markdown to html for conversation chat
    // Jsoup library is used to clean out any script and unwanted html tags

    // use Jsoup to allow certain tags for parsing
    Whitelist allowedTags = Whitelist.none(); // no tags allowed, empty whitelist
    // now add tags to empty whitelist
    // ins: underline, del: strikethrough, strong: bold
    // em: italics, a: link (+ href), img: image (+ src)
    allowedTags.addTags("ins", "del", "strong", "em", "img", "span");
    allowedTags.addAttributes("span", "href", "class");
    allowedTags.addAttributes("a", "href", "title");
    allowedTags.addAttributes("img", "alt", "height", "src", "title", "style");
    allowedTags.addProtocols("a", "href", "ftp", "http", "https", "mailto");
    allowedTags.addProtocols("img", "src", "http", "https");


    // this allows for extensions to be added for markdown
    MutableDataSet options = new MutableDataSet();

    // set underline(++) and strikethrough(~~) and autolink extension for markdown
    options.set(Parser.EXTENSIONS, Arrays.asList(InsExtension.create(),
    StrikethroughExtension.create())); //, AutolinkExtension.create()

    // parse markdown to html
    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    // re-use parser and renderer instances
    Node document = parser.parse(messageText);

    String markdownContent = renderer.render(document);
    // this deletes new line tag that parse auto creates at end of node
    markdownContent = markdownContent.replaceAll("\n", "");

    // this removes the new line from forming before acceptable html tags
    OutputSettings settings = new OutputSettings();
    settings.prettyPrint(false);

    // this removes any style / script / html (other than allowed tags) from the message content
    String cleanedMessageContent = Jsoup.clean(markdownContent, "", allowedTags, settings);

    //this code will check for emojis in message
    // this checks if message is single emoji, if not just skip to parse html
    if (isOneEmoji(cleanedMessageContent)) {
      //parse single emoji and double check there is an actual emoji
      cleanedMessageContent = EmojiParser.parseToUnicode(cleanedMessageContent);
      cleanedMessageContent = EmojiParser.parseToHtmlDecimal(cleanedMessageContent);
      if (!cleanedMessageContent.contains(":")) {
        cleanedMessageContent = "<span class=\"singleEmoji\">" + cleanedMessageContent + "</span>";
      }
    }
    //this parses :emojis: to unicode and html
    cleanedMessageContent = EmojiParser.parseToUnicode(cleanedMessageContent);
    cleanedMessageContent = EmojiParser.parseToHtmlDecimal(cleanedMessageContent);

    // this code will go through the message to find if it's an image link or regular message
    // first check if message contains image link
    int photoCount = 0;
    if(cleanedMessageContent.contains("photo link: ")) {
      photoCount = StringUtils.countMatches(cleanedMessageContent, "photo link: ");
      System.out.println(photoCount);
      cleanedMessageContent = checkPhotoLink(cleanedMessageContent, photoCount);
    }

    Message message =
        new Message(
            messageID,
            conversation.getId(),
            user.getId(),
            cleanedMessageContent,
            Instant.now());

    messageStore.addMessage(message);

    // create activity with type NEW_MESSAGE and add to activity list
    activityStore.addActivity(
        new Activity(
            Activity.Type.NEW_MESSAGE,
            message.getId(),
            message.getCreationTime(),
            UUID.randomUUID()));

    // redirect to a GET request
    response.sendRedirect("/chat/" + conversationTitle);
  }
}
