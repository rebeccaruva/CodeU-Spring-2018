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

package codeu.configuration;

import java.io.*;

/** servlet responsible for creating json file with credentials for Google APIs */
public class JsonConfiguration {
  /* create Json file */
  public static void createJson() {
    String jsonFile =
        "{\n\"type\": \"service_account\", \n\"project_id\": \"the-java-llamas-imhere\", \n\"private_key_id\": \""
            + System.getenv("PRIVATE_KEY")
            + "\", \n\"private_key\": \""
            + System.getenv("PRIVATE_KEY")
            + "\", \n\"client_email\": \"cloudkms@the-java-llamas-imhere.iam.gserviceaccount.com\", \n\"client_id\": \"115664706384991036873\", \n\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\", \n\"token_uri\": \"https://accounts.google.com/o/oauth2/token\", \n\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\", \n\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/cloudkms%40the-java-llamas-imhere.iam.gserviceaccount.com\"\n}";

    try {
      // write credentials to file - set path to whatever you set GOOGLE_APPLICATION_CREDENTIALS to
      BufferedWriter writer =
          new BufferedWriter(
              new FileWriter("C:\\Users\\alish\\OneDrive\\CodeU-Spring-2018\\IMhere.json"));
      writer.write(jsonFile);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
