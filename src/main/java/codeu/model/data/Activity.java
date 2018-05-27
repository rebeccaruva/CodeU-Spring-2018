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

import java.time.*;
import java.util.*;
import java.text.*;

/** Class representing a registered user. */
public abstract class Activity {
  /** Returns the ID of this Activity. */
  public abstract UUID getId();

  /** Returns the creation time of this Activity. */
  public abstract Instant getCreationTime();

  public abstract String getTitle();

  /**
    Types distinguish activity type:
    * 0 - user registered
    * 1 - user logged in
    * 2 - new conversation
    * 3 - message posted
  */
  public abstract void setType(int type);

  public abstract int getType();

  public abstract String getUser();

  public abstract String getConversation();

  public String formattedTime(){
    Date date = Date.from(this.getCreationTime());
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    return formatter.format(date) + " PST";
  }
}
