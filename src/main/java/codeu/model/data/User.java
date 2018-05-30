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

/** Class representing a registered user. */
public class User {
  private final String name;
  private final String passwordHash;
  private final UUID id;
  private final Instant creation;
  private Boolean admin;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password of this User
   * @param creation the creation time of this User
   * @param admin the admin status of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation, Boolean admin) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
    this.admin = admin;
  }

  /** Returns the ID of this Activity. */
  public UUID getId() {
    return id;
  }

  /** Returns the creation time of this Activity. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the admin status of this User. */
  public Boolean getAdminStatus() {
    if (getName().equals("admin")) return true;
    if (admin != null) return admin;
    else return false;
  }

  /** Gives the User admin status. */
  public void giveUserAdminStatus() {
    admin = true;
  }
}
